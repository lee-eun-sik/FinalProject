package back.controller.write;

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
import back.model.write.Write;
import back.model.write.Comment;
import back.model.common.CustomUserDetails;
import back.model.user.User;
import back.service.write.WriteService;
import back.service.write.WriteServiceImpl;
import back.util.ApiResponse;
import back.util.SecurityUtil;


@RestController
@RequestMapping("/api/write")
@Slf4j
public class WriteController{
	
@Autowired
private WriteService writeService;

/**
 * 
 * 게시글 목록 조회 (페이징 + 검색조건)
 */
@PostMapping("/list.do")
public ResponseEntity<?> getWriteList(@RequestBody Write write) {
	log.info(write.toString());
	List<Write> writeList = writeService.getWriteList(write);
	Map dataMap = new HashMap();
	dataMap.put("list", writeList);
	dataMap.put("totalPages", write.getTotalPages());
	dataMap.put("totalCount", write.getTotalCount());
	dataMap.put("page", write.getPage());
	return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", dataMap));
}

/**
 * 
 * 게시글 단건 조회
 */
@PostMapping("/view.do")
public ResponseEntity<?> getWrite(@RequestBody Write write) {
	Write selectWrite = writeService.getWriteById(write.getWritingId());
	return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공", selectWrite));
}

/**
 * 
 * 게시글 등록
 * @throws IOException 
 * @throws NumberFormatException 
 */
@PostMapping(value = "/create.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> createWrite (
	@ModelAttribute Write write,
	@RequestPart(value = "files", required = false) List<MultipartFile> files
	) throws NumberFormatException, IOException {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);
		write.setCreateId(userDetails.getUsername());
		write.setFiles(files);
		boolean isCreated = writeService.createWrite(write);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "게시글 등록 성공" : "게시글 등록 실패", null));
}

/**
 * 
 * 게시글 수정
 */
@PostMapping(value = "/update.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> updateWrite (
	@ModelAttribute Write write,
	@RequestPart(value = "files", required = false) List<MultipartFile> files
	) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);
		write.setUpdateId(userDetails.getUsername());
		write.setFiles(files);
		boolean isUpdated = writeService.updateWrite(write);
		return ResponseEntity.ok(new ApiResponse<>(isUpdated, isUpdated ? "게시글 수정 성공" : "게시글 수정 실패", null));
}

/**
 * 
 * 게시글 삭제
 */
@PostMapping(value = "/delete.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> deleteWrite (
	@ModelAttribute Write write,
	@RequestPart(value = "files", required = false) List<MultipartFile> files
	) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);
		write.setUpdateId(userDetails.getUsername());
		write.setFiles(files);
		boolean isDeleted = writeService.deleteWrite(write);
		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "게시글 삭제 성공" : "게시글 삭제 실패", null));
}

/**
 * 
 * 댓글 등록
 */
@PostMapping("/comment/create.do")
public ResponseEntity<?> createComment (@RequestBody Comment comment) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);
		comment.setCreateId(userDetails.getUsername());
		boolean isCreated = writeService.createComment(comment);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "댓글 등록 성공" : "댓글 등록 실패", null));
}

/**
 * 
 * 댓글 수정
 */
@PostMapping("/comment/update.do")
public ResponseEntity<?> updateComment (@RequestBody Comment comment) {

		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		
		SecurityUtil.checkAuthorization(userDetails);
		comment.setUpdateId(userDetails.getUsername());
		boolean isUpdated = writeService.updateComment(comment);
		return ResponseEntity.ok(new ApiResponse<>(isUpdated, isUpdated ? "댓글 수정 성공" : "댓글 수정 실패", null));
}

/**
 * 
 * 댓글 삭제
 */
@PostMapping("/comment/delete.do")
public ResponseEntity<?> deleteComment (@RequestBody Comment comment) {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);
		comment.setUpdateId(userDetails.getUsername());
		boolean isDeleted = writeService.deleteComment(comment);
		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "댓글 삭제 성공" : "댓글 삭제 실패", null));
}


}