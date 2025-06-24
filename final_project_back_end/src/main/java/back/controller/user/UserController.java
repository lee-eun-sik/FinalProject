package back.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import back.model.common.CustomUserDetails;
import back.model.user.User;
import back.service.email.EmailServiceImpl;
import back.service.user.UserService;
import back.util.ApiResponse;
import back.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
	
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailServiceImpl emailService;
	
	
	//사용자 정보 조회
	@PostMapping("/view.do")
	public ResponseEntity<?> view(
			@RequestBody User user,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		SecurityUtil.checkAuthorization(userDetails);
		
		String userId = "";
		if (user.getUsersId() == null || user.getUsersId().equals("")) {
			userId = userDetails.getUser().getUsersId();
		} else {
			userId = user.getUsersId();
		}
		User selectUser = userService.getUserById(userId);
		return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공", selectUser));
	}
	
	
	//회원가입
	@PostMapping("/register.do")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getUsersId() == null || user.getUsersPassword() == null || user.getUsersEmail() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "필수 입력값이 누락되었습니다.", null));
        }
        
        user.setCreateId("SYSTEM");
        boolean success = userService.registerUser(user);
        if (success) {
            // 회원가입 성공 후 AUTH_INFO 테이블에 usersId 저장
            emailService.updateUsersIdToAuthInfo(user.getUsersEmail(), user.getUsersId());
        }
        return ResponseEntity.ok(new ApiResponse<>(success, success ? "회원가입 성공" : "회원가입 실패", null));
    }
    
	//아이디 중복 체크
	@PostMapping("/checkUserId.do")
    public ResponseEntity<?> checkUserId(@RequestBody Map<String, String> request) {
        String usersId = request.get("usersId");
        boolean isDuplicate = userService.isUserIdDuplicate(usersId);
        Map<String, Object> result = new HashMap<>();
        result.put("available", !isDuplicate);
        return ResponseEntity.ok(result);
    }
		
	
	//회원정보 수정
	@PostMapping(value="/update.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> update(
			@ModelAttribute User user,
			@RequestPart(value="files", required = false)List<MultipartFile>files){
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		log.info("회원정보 수정 요청 : {}", user.getUsersId());
		user.setFiles(files); 
		user.setUpdateId(userDetails.getUsername());
		boolean success = userService.updateUser(user);
		return ResponseEntity.ok(new ApiResponse<>(success, success ? "수정 성공" : "수정 실패", userDetails.getUser()));

	}
	
	//회원탈퇴
	@PostMapping("/delete.do")
	public ResponseEntity<?> delete(@RequestBody User user, HttpSession session) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		log.info("회원탈퇴 요청 : {}", user.getUsersId());
		SecurityUtil.checkAuthorization(userDetails, userDetails.getUser().getUsersId());
		user.setUpdateId(userDetails.getUsername());
		boolean success = userService.deleteUser(user);
		if (success) {
			session.invalidate();
			SecurityContextHolder.clearContext();
		}
		return ResponseEntity.ok(new ApiResponse<>(success, success ? "삭제 성공" : "삭제 실패", null));
	}
	
	//로그인
	@PostMapping("/login.do")
	public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request) {
	    log.info("로그인 시도: {}", user.getUsersId());
	    try {
	        Authentication auth = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(user.getUsersId(), user.getUsersPassword())
	        );
	        SecurityContextHolder.getContext().setAuthentication(auth);
	        HttpSession session = request.getSession(true);
	        session.setAttribute(
	            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
	            SecurityContextHolder.getContext()
	        );
	        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
	        return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공", userDetails.getUser()));
	    } catch (AuthenticationException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(new ApiResponse<>(false, "아이디 또는 비밀번호가 일치하지 않습니다.", null));
	    }
	}
    
	//로그아웃
	@PostMapping("/logout.do")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse<>(true, "로그아웃 완료", null));
    }
	
	//게시물 정보 조회
	@PostMapping("/list.do")
	public ResponseEntity<?> getUserList(@RequestBody User user) {
		log.info(user.toString());
		List<User> userList = userService.getUserList(user);
		Map dataMap = new HashMap();
		dataMap.put("list", userList);
		dataMap.put("user", user);
		return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", dataMap));
	}
	
	//회원관리(사용하지 않는 기능)
	@PostMapping("/userM.do")
	public ResponseEntity<?> userM(@RequestBody User user) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		log.info("회원관리 요청 : {}", user.getUsersId());
		user.setUpdateId(userDetails.getUsername());
		boolean success = userService.userM(user);
		return ResponseEntity.ok(new ApiResponse<>(success, success ? "회원관리 성공" : "회원관리 실패", null));
	}

}