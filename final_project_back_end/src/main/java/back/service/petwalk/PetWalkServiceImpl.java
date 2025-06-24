package back.service.petwalk;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import back.mapper.petwalk.PetWalkMapper;
import back.mapper.file.FileMapper;
import back.model.pet.Pet;
import back.model.common.PostFile;
import back.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PetWalkServiceImpl implements PetWalkService {

	@Autowired
    private PetWalkMapper petWalkMapper;
	
	@Autowired
    private FileMapper fileMapper;
	
	@Override
	public boolean petWalkSave(Pet pet) throws NumberFormatException, IOException {
		boolean result = petWalkMapper.petWalkSave(pet) > 0;
		
		return result;
	}
	
	@Override
	public boolean petWalkUpdate(Pet pet) throws NumberFormatException, IOException {
		boolean result = petWalkMapper.petWalkUpdate(pet) > 0;
		
		return result;
	}
	

    public PostFile getFileByFileId(PostFile file) { 
        PostFile PostFile = petWalkMapper.getFileByFileId(file);
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
	            	petWalkMapper.insertFile(postFile);
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
	@Transactional
	public boolean imgSave(Pet pet) throws NumberFormatException, IOException {
		
			List<MultipartFile>files = pet.getFiles();
		
			//업로드된 파일들을 처리하여 PostFile 객체 리스트 반환
			List<PostFile> fileList = FileUploadUtil.uploadFiles(
					files,                  // 받아온 파일
					"petWalk",				// 폴더이름
					pet.getWalkId(),		// 파일테이블에넣을 고유키값   walkId 넣어야함
					"WAL",					// 파일테이블에넣을 카테고리  WAL 넣어야함
					pet.getCreateId()		// 로그인중인 유저아이디(createId = 유저이름)
				);
			for (PostFile PostFile : fileList) {
				fileMapper.insertFile(PostFile);
			}
		return true;
	}

	@Override
	public List<PostFile> awalkIdSearch(PostFile postFile) {
		
		log.info("파일 저장 경로: {}", postFile.getPostFilePath());
		return fileMapper.getAllFiles(postFile);
	}


	@Override
	public List<Pet> getCurrentWalkId(Pet pet) throws NumberFormatException, IOException {
		
		return petWalkMapper.getCurrentWalkId(pet);
	}

	@Override
	public List<Pet> petCurrentLoad(Pet pet) throws NumberFormatException, IOException {

		
		return petWalkMapper.petCurrentLoad(pet);
	}


}