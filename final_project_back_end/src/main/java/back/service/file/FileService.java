package back.service.file;


import java.util.Map;

import back.model.common.PostFile;

public interface FileService {
	
    public PostFile getFileByFileId(int postFileId);
    
    public Map<String, Object> insertFiles(PostFile file);  

}