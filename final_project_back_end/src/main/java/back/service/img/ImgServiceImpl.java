package back.service.img;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import back.mapper.img.ImgMapper;
import back.mapper.file.FileMapper;
import back.model.write.Write;
import back.model.common.PostFile;
import back.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImgServiceImpl implements ImgService {

	@Autowired
    private ImgMapper imgMapper;
	
	@Autowired
    private FileMapper fileMapper;
	

    public PostFile getFileByFileId(PostFile file) { 
        PostFile PostFile = imgMapper.getFileByFileId(file);
        return PostFile;
    }

    @Transactional
    public Map<String, Object> insertBoardFiles(PostFile file) { 
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
	            	imgMapper.insertFile(postFile);
	            }
	            resultMap.put("result", true);
	
	            if(uploadedFiles != null && uploadedFiles.size() > 0) {
	                resultMap.put("fileId", uploadedFiles.get(0).getPostFileId());
	            }
	            return resultMap;

        } catch (Exception e) {

        }
        return resultMap;
    }

	@Override
	public boolean imgSave(Write board) throws NumberFormatException, IOException {
		
			//업로드된 파일들을 처리하여 PostFile 객체 리스트 반환
			List<PostFile> fileList = FileUploadUtil.uploadFiles(board.getFiles(), "pj4");
			for (PostFile PostFile : fileList) {
				fileMapper.insertFile(PostFile);
			}
		return true;
	}

	@Override
	public List<PostFile> getAllFiles(PostFile postFile) {
		// TODO Auto-generated method stub
		log.info("파일 저장 경로: {}", postFile.getPostFilePath());
		return fileMapper.getAllFiles(postFile);
	}
}