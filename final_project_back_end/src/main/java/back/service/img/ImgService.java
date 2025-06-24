package back.service.img;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import back.model.write.Write;
import back.model.common.PostFile;

public interface ImgService {
    
	public boolean imgSave(Write board) throws NumberFormatException, IOException;
	
    public PostFile getFileByFileId(PostFile file);
    
    public Map<String, Object> insertBoardFiles(PostFile file);
    
    public List<PostFile> getAllFiles(PostFile postFile);
}