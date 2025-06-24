package back.service.diary;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import back.mapper.diary.DiaryMapper;
import back.mapper.file.FileMapper;
import back.model.common.PostFile;
import back.model.diary.Diary;

import back.util.FileUploadUtil;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DiaryServiceImpl implements DiaryService{
	@Autowired
	private DiaryMapper diaryMapper;

	@Autowired
	private FileMapper fileMapper;
	
	@Override
	@Transactional
	public boolean createDiary(Diary diary) {
		try {
			boolean result = diaryMapper.create(diary)>0;
			
			List<MultipartFile>files=diary.getFiles();
			Integer id = diary.getDiaryId();
			if(result && files != null && id != null && id > 0) {
				List<PostFile> fileList = FileUploadUtil.uploadFiles(
						files, 
						"diary",
						diary.getDiaryId(),
						"MMO",
						diary.getCreateId()
					);
				
			for(PostFile postFile : fileList) {
				boolean insertResult = fileMapper.insertFile(postFile)>0;
				if(!insertResult) throw new HException("파일 추가 실패");
				log.info("업로드된 파일 수: {}", fileList.size());
			}
			}
			return result;
		}catch(Exception e) {
			log.error("일기 저장 실패 userId={}, title={}", diary.getUsersId(), diary.getDiaryTitle(), e);
			throw new HException("일기 저장 실패", e);
			}
	}

	@Override
	public List<Diary> getDiaryList(Diary diary) {
		try {
			
			List<Diary> diaryList= diaryMapper.getDiaryList(diary);
			for(Diary d:diaryList) {
				PostFile file = new PostFile();
				String category = diary.getPostFileCategory();
				if (category == null || category.isEmpty()) {
			        category = "MMO"; // 기본값
			    } // 기본값 지정
				file.setPostFileCategory(category);
				file.setPostFileKey(d.getDiaryId());
				List<PostFile> files=fileMapper.getFilesByFileKey(file);
				if(files !=null && !files.isEmpty()) {
					d.setPostFiles(List.of(files.get(0)));
				}
			}
			return diaryList;
		}catch(Exception e) {
			log.error("게시글 목록 조회 실패", e);
			throw new HException("게시글 목록 조회 실패",e);
		}
	}

	@Override
	public Diary getDiaryById(int diaryId) {
		try {
			Diary diary=diaryMapper.getDiaryById(diaryId);
			PostFile file = new PostFile();
			String category = diary.getPostFileCategory();
			if (category == null || category.isEmpty()) {
		        category = "MMO"; // 기본값
		    } // 기본값 지정
			file.setPostFileCategory(category);
			file.setPostFileKey(diary.getDiaryId());
			diary.setPostFiles(fileMapper.getFilesByFileKey(file));
			return diary;
		}catch(Exception e) {
			log.error("일기 조회 실패", e);
			throw new HException("일기 조회 실패", e);
		}
	}
	@Override
	@Transactional
	public boolean updateDiary(Diary diary) {
		try {
			boolean result = diaryMapper.update(diary)>0;
			if(result) {
				List<MultipartFile>files=diary.getFiles();
				String remainingFileIds = diary. getRemainingFileIds(); 
				PostFile file = new PostFile();
				String category = diary.getPostFileCategory();
				if (category == null || category.isEmpty()) {
			        category = "MMO"; // 기본값
			    } // 기본값 지정
				file.setPostFileCategory(category);
				file.setPostFileKey(diary.getDiaryId());
				List<PostFile> existingFiles = fileMapper.getFilesByFileKey(file);
				
				for(PostFile existing:existingFiles) {
					if(!remainingFileIds.contains(String.valueOf(existing.getPostFileId()))) {
						existing.setUpdateId(diary.getUpdateId());
						boolean deleteResult = fileMapper.deleteFile(existing)>0;
						if(!deleteResult) throw new HException("파일 삭제 실패");
					}
				}
				
				String createId = (diary.getCreateId() != null) ? diary.getCreateId() : diary.getUpdateId();//createId null값 대체
				if (files != null && !files.isEmpty()) {
					List<PostFile> uploadedFiles = FileUploadUtil.uploadFiles(
							files,
							"diary",
							diary.getDiaryId(),
							category,
							createId
							);
					for(PostFile postFile : uploadedFiles) {
						boolean insertResult = fileMapper.insertFile(postFile)>0;
						if(!insertResult) throw new HException("파일 추가 실패");
					}
				}
				log.info("remainingFileIds: {}", remainingFileIds);
			}
			return result;
		}catch (Exception e) {
			log.error("일기 수정 실패",e);
			throw new HException("일기 수정 실패",e);
			}
	}
	@Override
	public boolean deleteDiary(Diary diary) {
		try {
			return diaryMapper.delete(diary)>0;
		}catch (Exception e) {
			log.error("게시글 삭제 실패",e);
			throw new HException("게시글 삭제 실패",e);
		}
	}
}
