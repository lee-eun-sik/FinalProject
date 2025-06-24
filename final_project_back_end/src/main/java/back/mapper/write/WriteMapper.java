package back.mapper.write;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.write.Write;
import back.model.write.Comment;

@Mapper
public interface WriteMapper {
	
	public List<Write> getWriteList(Write write);
	
	public int getTotalWriteCount(Write write);
	
	public Write getWriteById(int WritingId);
	
	public int create(Write write);
	
	public int update(Write write);
	
	public int delete(Write write);
	
	public int incrementViewCount(int WritingId);
	
	public List<Comment> getCommentsByWriteId(int WritingId);
	
	public int insertComment(Comment comment);
	
	public int updateComment(Comment comment);
	
	public int deleteComment(Comment comment);
	
}
