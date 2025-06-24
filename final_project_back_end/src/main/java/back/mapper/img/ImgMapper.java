package back.mapper.img;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.common.PostFile;

@Mapper
public interface ImgMapper {
	
	public int insertFile(PostFile file);
	
	public PostFile getFileByFileId(PostFile file);
	
	public List<PostFile> getFilesByBoardId(String BoardId);
	
	public int deleteFile(PostFile file);
	
	public List<PostFile> getAllFiles(); // 파일 전체 가져오기
	
}
