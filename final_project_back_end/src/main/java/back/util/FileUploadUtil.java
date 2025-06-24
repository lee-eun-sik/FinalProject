package back.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import back.model.common.PostFile;

public class FileUploadUtil {
    private static final String UPLOAD_DIR = "uploads"; // 최상위 업로드 디렉토리

    /**
     * 다중 파일 업로드 처리
     */
    public static List<PostFile> uploadFiles(List<MultipartFile> multipartFiles, String basePath, int postFileKey, String postFileCategory, String usersName) throws IOException {
        List<PostFile> uploadedFiles = new ArrayList<>();
        
        // multipartFiles가 null일 경우 빈 리스트 반환
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return uploadedFiles;
        }
        
        // yyyy-MM-dd 형식으로 단일 날짜 폴더 구성
        String dateFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String uploadPath = getUploadPath(basePath, dateFolder);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 폴더 없으면 생성
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");

        for (MultipartFile file : multipartFiles) {
            String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();  // 실제파일이름

            if (!originalFileName.isEmpty()) {
            	                      //알파벳(a-zA-Z), 숫자(0-9), 점(.), 언더바(), 대시(-) 이외의 모든 문자를 언더바()로 대체
                String safeFileName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");

                String fileExtension = "";
                int dotIndex = safeFileName.lastIndexOf(".");  //파일명.확장자  에서 .위치찾음
                if (dotIndex > 0) {
                    fileExtension = safeFileName.substring(dotIndex);  // . 기준 뒤에는 확장자 확인
                    safeFileName = safeFileName.substring(0, dotIndex); // . 기준 앞에는 이름 확인
                }

                String timestamp = sdf.format(new Date()) + "_" + System.nanoTime();
                String newFileName = timestamp + "_" + safeFileName + fileExtension;

                String filePath = uploadPath + File.separator + newFileName;

                file.transferTo(new File(filePath));
                
                PostFile postFile = new PostFile();
                postFile.setPostFileCategory(postFileCategory); // < 카테고리 산책 WAL / 일기 MMO / 커뮤니티 COM 
                postFile.setPostFileKey(postFileKey);   //   <<여기에 산책, 일기, 커뮤니티등의  고유키값(walkId, diaryId 등등) 들어감
                postFile.setPostFileName(originalFileName);   // < 파일의 실제이름  위에서 만듬
                postFile.setPostFilePath(filePath); // 실제 파일 전체 경로  위에서만듬
                postFile.setCreateId(usersName);      // 받아온 유저네임   createId 를 보내오는데 createId = get.UserName
                postFile.setUpdateId(usersName);	  // 받아온 유저네임   createId 를 보내오는데 createId = get.UserName

                uploadedFiles.add(postFile);
            }
        }

        return uploadedFiles;
    }

    
    
    /**
     * 업로드 경로 반환 (날짜 단일 폴더 포함)
     */
    public static String getUploadPath(String basePath, String dateFolder) {
        return File.separator + UPLOAD_DIR + File.separator + basePath + File.separator + dateFolder;
    }
    
    
    /**
     * 
     * 임시기능
     */
    public static List<PostFile> uploadFiles(List<MultipartFile> multipartFiles, String basePath) throws NumberFormatException, IOException {
        List<PostFile> uploadedFiles = new ArrayList<>();
        
        // multipartFiles가 null일 경우 빈 리스트 반환
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return uploadedFiles;
        }
        
        // yyyy-MM-dd 형식으로 단일 날짜 폴더 구성
        String dateFolder = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String uploadPath = getUploadPath(basePath, dateFolder);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 폴더 없으면 생성
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");

        for (MultipartFile file : multipartFiles) {
            String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();  // 실제파일이름

            if (!originalFileName.isEmpty()) {
            	                      //알파벳(a-zA-Z), 숫자(0-9), 점(.), 언더바(), 대시(-) 이외의 모든 문자를 언더바()로 대체
                String safeFileName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");

                String fileExtension = "";
                int dotIndex = safeFileName.lastIndexOf(".");  //파일명.확장자  에서 .위치찾음
                if (dotIndex > 0) {
                    fileExtension = safeFileName.substring(dotIndex);  // . 기준 뒤에는 확장자 확인
                    safeFileName = safeFileName.substring(0, dotIndex); // . 기준 앞에는 이름 확인
                }

                String timestamp = sdf.format(new Date()) + "_" + System.nanoTime();
                String newFileName = timestamp + "_" + safeFileName + fileExtension;

                String filePath = uploadPath + File.separator + newFileName;

                file.transferTo(new File(filePath));

                PostFile postFile = new PostFile();
                postFile.setPostFileCategory("WAL");
                postFile.setPostFileKey(1);  //  <<여기에 산책, 일기, 커뮤니티 등 고유키 들어감
                postFile.setPostFileName(originalFileName);
                postFile.setPostFilePath(filePath); // 실제 파일 전체 경로
                postFile.setCreateId("test");
                postFile.setUpdateId("test");
                postFile.setDelYn("N");

                uploadedFiles.add(postFile);
            }
        }

        return uploadedFiles;
    }
}
