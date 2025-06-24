package back.model.test;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import back.model.Model;
import back.model.common.PostFile;
import back.model.diary.Diary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자 자동 생성
@EqualsAndHashCode(callSuper = true)
public class Test extends Model{
	private Integer testQuestionId;
	private Integer testOptionId;
	private Integer testResultId;
	
	private String testQuestionCategory;
	private String testQuestionContent;
	private String testQuestionType;
	
	private String testOptionContent;
	private int testOptionScore;
	
	private String testResultCategory;
	private Integer fileId;
	private String testResultName;
	private String testResultContent;
	private String testResultRecommend;
	private String testResultType;
	
	private List<Integer> questionIds;
	private List<Integer> optionIds;  
	private PostFile postFile;
}
