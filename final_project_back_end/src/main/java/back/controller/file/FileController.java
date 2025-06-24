package back.controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import back.model.common.PostFile;
import back.service.file.FileService;
import back.util.ApiResponse;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/file")
@Slf4j
public class FileController {

	@Value("${myapp.apiBaseUrl}")
	private String apiBaseUrl;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ServletContext servletContext;
	
	/**
	 * 
	 * 일반 파일 다운로드
	 */
	@GetMapping("/down.do")
	public void downloadFile(@RequestParam("fileId")String fileId, HttpServletResponse response) {
		try {
			PostFile file = new PostFile();
			file.setPostFileId(Integer.parseInt(fileId));
			int postFileId = Integer.parseInt(fileId);
			PostFile selectFile = fileService.getFileByFileId(postFileId);
			
			if (selectFile == null) {
				response.getWriter().write("파일을 찾을 수 없습니다.");
				return;	
			}
			
			File downloadFile = new File(selectFile.getPostFilePath());
			if (!downloadFile.exists() ) {
				response.getWriter().write("파일이 존재하지 않습니다.");
				return;
			}
			
			String fileName = selectFile.getPostFileName();
			response.setContentType("application/octet-stream");
			response.setContentLength((int) downloadFile.length());
			response.setHeader("Content-Disposition",
					"attachment; filename=" + URLEncoder.encode(selectFile.getPostFileName(), "UTF-8"));
			
			try (
					FileInputStream fis = new FileInputStream(downloadFile);
					OutputStream out = response.getOutputStream()
				) {
					byte[] buffer = new byte[4096];
					int bytesRead;
					
					while ((bytesRead = fis.read(buffer)) != -1) {
						out.write(buffer, 0, bytesRead);
					}
			}
			
		} catch (Exception e) {
			log.error("파일 다운로드 오류", e);
		}
	}
	
	
	@GetMapping("/imgDown.do")
	public void downloadImage(@RequestParam("fileId")String PostFileId, HttpServletResponse response) {
		try {
			PostFile file = new PostFile();
			file.setPostFileId(Integer.parseInt(PostFileId));
			int postFileId = Integer.parseInt(PostFileId);
			PostFile selectFile = fileService.getFileByFileId(postFileId);
			
			if (selectFile == null) {
				response.getWriter().write("파일을 찾을 수 없습니다.");
				return;	
			}
			
			File downloadFile = new File(selectFile.getPostFilePath());
			if (!downloadFile.exists() ) {
				response.getWriter().write("파일이 존재하지 않습니다.");
				return;
			}
			
			String mimeType = servletContext.getMimeType(selectFile.getPostFilePath());
			if (mimeType == null) mimeType = "application/octet-stream";
			
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());
			response.setHeader("Content-Disposition",
					"inline; filename=" + URLEncoder.encode(selectFile.getPostFileName(), "UTF-8"));
			
			try (
				FileInputStream fis = new FileInputStream(downloadFile);
				OutputStream out = response.getOutputStream()
			) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				
				while ((bytesRead = fis.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
		} catch (Exception e) {
			log.error("이미지 다운로드 오류", e);
		}
	}
	@PostMapping(value = "/imgUpload.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadImage(
			@ModelAttribute PostFile postFile,
			@RequestPart(value = "files", required = false) List<MultipartFile> files) {
		log.info("이미지 파일 업로드 요청");
		
		HashMap<String, Object> responseMap = new HashMap<>();
		postFile.setFiles(files);
		boolean isUploadFile = false;
		
		try {
			postFile.setBasePath("img");
			postFile.setCreateId("SYSTEM");
			
			HashMap resultMap = (HashMap) fileService.insertFiles(postFile);
			isUploadFile = (boolean) resultMap.get("result");
			
			responseMap.put("success", isUploadFile);
			responseMap.put("message", isUploadFile ? "이미지 파일 업로드 성공되었습니다." : "이미지 파일 업로드 실패");
			
			if (isUploadFile) {
				responseMap.put("url", apiBaseUrl+"/api/file/imgDown.do?fileId=" + resultMap.get("fileId"));
			}
			
		} catch (Exception e) {
			log.error("이미지 파일 업로드 중 오류", e);
			responseMap.put("success", false);
			responseMap.put("message", "서버 오류 발생");
		}
		
		return ResponseEntity.ok(new ApiResponse<>(isUploadFile,
				isUploadFile ? "이미지 파일 업로드 성공" : "이미지 파일 업로드 실패", responseMap));
		
	}
	
	
	
	
}