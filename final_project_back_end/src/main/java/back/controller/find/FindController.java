package back.controller.find;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import back.model.user.User;
import back.service.user.UserService;
import back.util.ApiResponse;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/find")
@Slf4j
@RequiredArgsConstructor
public class FindController {

    private final UserService userService;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/findId.do")
    public ResponseEntity<?> findUserIdByInfo(@RequestBody Map<String, String> body) {
       
        String email = body.get("usersEmail");

        log.info("사용자 정보로 ID 찾기 요청: {}, {}, {}, {}", email);

        List<User> users = userService.findUsersByInfo(email);
        if (users.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "일치하는 사용자 정보를 찾을 수 없습니다.", null));
        }

        List<Map<String, String>> userInfoList = users.stream()
            .map(user -> Map.of(
                "usersId", user.getUsersId(),
                "createDt", user.getCreateDt() == null ? "" : user.getCreateDt()
            ))
            .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "일치하는 사용자 정보를 찾았습니다.", Map.of("list", userInfoList)));
    }

    @PostMapping("/findPw.do")
    public ResponseEntity<?> findUserPwByInfo(@RequestBody Map<String, String> body) {
        String usersId = body.get("usersId");
        String usersEmail = body.get("usersEmail");

        User user = userService.findUserByUserIdAndEmail(usersId, usersEmail);
        if (user == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, "입력한 정보가 일치하지 않습니다.", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "입력하신 정보와 일치하는 계정이 존재합니다.", null));
    }
    
    
    @PostMapping("/resetPassword.do")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody Map<String, String> payload) {
        String usersId = payload.get("usersId");
        String newPassword = payload.get("newPassword");
        
        User user = new User();
        user.setUsersId(usersId);
        user.setUpdateId(usersId);
        user.setEncodedPassword(newPassword);

        if (usersId == null || newPassword == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "필수 정보 누락", null));
        }

        boolean result = userService.resetPassword(user);

        if (result) {
            return ResponseEntity.ok(new ApiResponse<>(true, "비밀번호가 변경되었습니다.", null));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "비밀번호 변경 실패", null));
        }
    }
}
