package back.model.diary;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import back.model.Model;
import back.model.common.PostFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자 자동 생성
@EqualsAndHashCode(callSuper = true)
public class Diary extends Model{
	
	private Integer  diaryId;
	private String diaryTitle;
	private String diaryContent;
	private String diaryDate;
	private String diaryType;
	
	private String thumbnail;
	
	private String sortField="DIARY_DATE";
	private String sortOrder="DESC";
	
	private List<PostFile> postFiles;
	private List<MultipartFile> files;
	private String postFileCategory;
	private String remainingFileIds;
}

