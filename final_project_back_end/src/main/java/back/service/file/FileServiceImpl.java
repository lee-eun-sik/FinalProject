package back.service.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import back.mapper.file.FileMapper;
import back.model.common.PostFile;
import back.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

	@Autowired
    private FileMapper fileMapper;

    public PostFile getFileByFileId(int postFileId) { 
        PostFile postFile = fileMapper.getFileByFileId(postFileId);
        return postFile;
    }

    @Transactional
    public Map<String, Object> insertFiles(PostFile file) { 
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
	        int postFileKey = file.getPostFileKey();
	        String usersId = file.getCreateId();
	        String basePath = file.getBasePath();
	        int postFileId = file.getPostFileId();
	        String postFileCategory = file.getPostFileCategory();
	        String postFileName=file.getPostFileName();
	
	        List<MultipartFile> files = file.getFiles();
	
	        if(files == null || files.isEmpty()) {
	            resultMap.put("result",false);
	            resultMap.put("message","파일이 존재하지 않습니다.");
	            return resultMap;
	        }
	
	        List<PostFile> uploadedFiles = FileUploadUtil.uploadFiles(files, basePath, postFileKey,  postFileCategory, usersId);
	
	            for (PostFile postFile : uploadedFiles) {
	                fileMapper.insertFile(postFile);
	            }
	            resultMap.put("result", true);
	
	            if(uploadedFiles != null && uploadedFiles.size() > 0) {
	                resultMap.put("fileId", uploadedFiles.get(0).getPostFileId());
	            }
	            return resultMap;

        } catch (Exception e) {
        	log.error("파일 업로드 중 오류 발생", e);
            resultMap.put("result", false);
            resultMap.put("message", "파일 업로드 실패: " + e.getMessage());
        }
        return resultMap;
    }
}