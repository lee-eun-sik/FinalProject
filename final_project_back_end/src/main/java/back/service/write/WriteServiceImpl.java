package back.service.write;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import back.mapper.file.FileMapper;
import back.mapper.write.WriteMapper;
import back.model.common.PostFile;
import back.model.write.Comment;
import back.model.write.Write;
import back.service.file.FileService;
import back.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class WriteServiceImpl implements WriteService {
    @Autowired
    private WriteMapper writeMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileService fileService; // 새로 추가: FileService 주입
    
    @Override
	public Write getWriteById(int writingId) {
    	
    	try {
    		 // 조회수 증가 로직 추가
            writeMapper.incrementViewCount(writingId); // 조회수 1 증가 [1]
            log.info("게시글 ID {}의 조회수를 증가시켰습니다.", writingId);

            Write write = writeMapper.getWriteById(writingId);
    	
    	 if (write != null) { // 게시글이 존재하는 경우에만 파일 조회
             PostFile searchFileCriteria = new PostFile();
             searchFileCriteria.setPostFileKey(write.getWritingId()); // 게시글의 ID를 파일 검색 조건으로 설정
             searchFileCriteria.setPostFileCategory("COM"); // 해당 게시판의 카테고리도 검색 조건으로 추가 (선택 사항이지만 안전함)

             // 파일 목록 조회
             write.setPostFiles(fileMapper.getFilesByFileKey(searchFileCriteria));
         }
         
         //댓글 목록 조회
         write.setComments(writeMapper.getCommentsByWriteId(writingId));
         return write;
     } catch (Exception e) {
         log.error("게시글 상세 조회 실패", e); // 0 대신 e를 찍어 스택 트레이스를 확인하는 것이 좋습니다.
         throw new HException("게시글 상세 조회 실패", e); // 예외 메시지를 명확히
     }
 }
	
    @Override
	@Transactional
	public boolean createWrite(Write write) throws NumberFormatException, IOException {
       
        	boolean result = writeMapper.create(write) > 0;
	
        	if (result) {
				String postFileCategory = "COM"; // 이 게시판 유형에 대한 카테고리
				
				List<PostFile> fileList = FileUploadUtil.uploadFiles(
				    write.getFiles(), 
				    "write", // basePath
				    write.getWritingId(), // postFileKey (실제 게시글과 파일을 연결합니다.)
				    postFileCategory, // postFileCategory
				    write.getCreateId() // usersId
				);
						
				for (PostFile postFile : fileList) {
					boolean createResult = fileMapper.insertFile(postFile) > 0;
					if (!createResult) {
		                throw new RuntimeException("파일 추가 실패: " + postFile.getPostFileName());
					}
				}
			}
			return result;
    
    }
	
	@Override
	@Transactional
	public boolean updateWrite(Write write) {
        try {
            boolean result = writeMapper.update(write) > 0;
            
            if (result) {
            	
            	List<MultipartFile> files = write.getFiles();
            	String remainingFileIds = write.getRemainingFileIds();
            	 Set<String> remainingFileIdSet = new HashSet<>(Arrays.asList(remainingFileIds.split(",")));
            	
            	 PostFile searchFile = new PostFile();
                 searchFile.setPostFileKey(write.getWritingId()); 
                 searchFile.setPostFileCategory("COM");

             	List<PostFile> existingFiles = fileMapper.getFilesByFileKey(searchFile);
            	

            	for (PostFile existing : existingFiles) {
            		if (!remainingFileIdSet.contains(String.valueOf(existing.getPostFileId()))) {
            			existing.setUpdateId(write.getUpdateId());
            			boolean deleteResult = fileMapper.deleteFile(existing) > 0 ;
            			if (!deleteResult) throw new HException("파일 삭제 실패");
            		}
            	}
            	
            	if (files != null && !files.isEmpty()) {
            		// PostFile 객체를 생성하여 FileService.insertFiles에 필요한 정보를 담습니다.
            		PostFile newFilesToUpload = new PostFile();
            		newFilesToUpload.setFiles(files); // 업로드할 MultipartFile 리스트 설정
            		newFilesToUpload.setPostFileKey(write.getWritingId()); // 게시글 ID를 POST_FILE_KEY로 설정
            		newFilesToUpload.setCreateId(write.getUpdateId()); // 생성자 ID (업데이트 시에는 업데이트 ID 사용)
            		newFilesToUpload.setUpdateId(write.getUpdateId()); // 업데이트 ID 설정
            		newFilesToUpload.setBasePath("write");
            		newFilesToUpload.setPostFileCategory("COM");

            		// FileService를 통해 파일 삽입 로직 호출
            		Map<String, Object> uploadResult = fileService.insertFiles(newFilesToUpload);

            		if (!(boolean) uploadResult.get("result")) {
            			log.error("게시글 수정 중 새 파일 업로드 및 DB 추가 실패: {}", uploadResult.get("message"));
            			throw new HException("파일 추가 실패: " + uploadResult.get("message"));
            		}
            	}
            }
            
            return result;
        } catch (Exception e) {
            log.error("게시글 수정 실패", 0);
            throw new HException("게시글 수정 실패", e);
        }
    }
	
	@Override
	@Transactional
	public boolean deleteWrite(Write write) {
		try {
			return writeMapper.delete(write) > 0;
		} catch (Exception e) {
			log.error("게시글 삭제 실패", e);
			throw new HException("게시글 삭제 실패", e);
		}
    }


	@Override
	@Transactional
	public List getWriteList(Write write) {
		
		int page = write.getPage();
		int size = write.getSize();
		
		int totalCount = writeMapper.getTotalWriteCount(write);
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		int startRow = (page - 1) * size + 1;
		int endRow = page *size;
		
		write.setTotalCount(totalCount);
		write.setTotalPages(totalPages);
		write.setStartRow(startRow);
		write.setEndRow(endRow);
		
		List list = writeMapper.getWriteList(write);
		
		return list;
	}
	
	@Override
	@Transactional
	public boolean createComment(Comment comment) {
		try {
			return writeMapper.insertComment(comment) > 0;
		} catch (Exception e) {
			log.error("댓글 생성 실패", e);
			throw new HException("댓글 생성 실패", e);
		}
	}
	
	@Override
	@Transactional
	public boolean updateComment(Comment comment) {
		try {
			return writeMapper.updateComment(comment) > 0;
		} catch (Exception e) {
			log.error("댓글 수정 실패", e);
			throw new HException("댓글 수정 실패", e);
		}
	}
	
	@Override
	@Transactional
	public boolean deleteComment(Comment comment) {
		try {
			return writeMapper.deleteComment(comment) > 0;
		} catch (Exception e) {
			log.error("댓글 삭제 실패", e);
			throw new HException("댓글 삭제 실패", e);
		}
	}
	


}