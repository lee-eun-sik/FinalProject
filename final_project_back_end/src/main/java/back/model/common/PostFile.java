package back.model.common;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import back.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // 기본 생성자 자동 생성
@AllArgsConstructor  // 모든 필드를 매개변수로 하는 생성사 자동 생성
@EqualsAndHashCode(callSuper = true)

public class PostFile extends Model {
	private int postFileId;
	private String postFileCategory;
	private int postFileKey;
	private String postFilePath;
	private String basePath;
	private String postFileName;
	
	private List<MultipartFile> files; 

}
