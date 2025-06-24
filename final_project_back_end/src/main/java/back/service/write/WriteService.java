package back.service.write;

import java.io.IOException;
import java.util.List;

import back.model.write.Write;
import back.model.common.PostFile;
import back.model.write.Comment;

public interface WriteService {
    
	public List getWriteList(Write write);
	
    public Write getWriteById(int writingId);
    
    public boolean createWrite(Write write) throws NumberFormatException, IOException;
    
    public boolean updateWrite(Write write);
    
    public boolean deleteWrite(Write write);
    
    public boolean createComment(Comment comment);
    
    public boolean updateComment(Comment comment);
    
    public boolean deleteComment(Comment comment);


}