package back.controller.combo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import back.model.combo.Combo;
import back.model.combo.CommonCode;
import back.model.combo.GroupCode;
import back.model.common.CustomUserDetails;
import back.model.user.User;
import back.service.combo.ComboService;
import back.util.ApiResponse;
import back.util.SecurityUtil;


@RestController
@RequestMapping("/api/combo")
@Slf4j
public class ComboController{
	
@Autowired
private ComboService comboService;


@PostMapping("/create.do")
public ResponseEntity<?> createBoard (@RequestBody Combo combo) {
//		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
//				.getAuthentication().getPrincipal();
//		SecurityUtil.checkAuthorization(userDetails);
		boolean isCreated = comboService.create(combo);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "게시글 등록 성공" : "게시글 등록 실패", null));
}

@PostMapping("/delete.do")
public ResponseEntity<?> delete(@RequestBody Combo combo) {
    boolean deleted = comboService.delete(combo);
    return ResponseEntity.ok(new ApiResponse<>(deleted, deleted ? "삭제 성공" : "삭제 실패", null));
}

//정해진 테이블, 컬럼 조회
@PostMapping("/list.do")
public ResponseEntity<?> getComboList(@RequestBody Combo combo) {
	log.info(combo.toString());
	List comboList = comboService.getList(combo);

	return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", comboList));
}

//활성화된 그룹아이디로 COMMON 조회
@PostMapping("/common.do")
public ResponseEntity<List<GroupCode>> getGroupsWithCodes() {
    List<GroupCode> result = comboService.getActiveGroupsWithCodes();
    return ResponseEntity.ok(result);
}

//받아온 그룹아이디로 COMMON 조회
@PostMapping("/listByGroup.do")
public ResponseEntity<?> getComboListByGroup(@RequestBody Map<String, String> param) {
    String groupId = param.get("groupId");
    List<CommonCode> comboList = comboService.getListByGroupId(groupId);
    return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", comboList));
}



}