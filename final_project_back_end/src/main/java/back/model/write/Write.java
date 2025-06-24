package back.model.write;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import back.model.Model;
import back.model.write.Comment;
import back.model.common.PostFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // 기본 생성자 자동 생성
@AllArgsConstructor  // 모든 필드를 매개변수로 하는 생성사 자동 생성
@EqualsAndHashCode(callSuper = true)
public class Write extends Model {

	//검색필터
	private String searchText;
	private String startDate;
	private String endDate;
	
	//페이징
	private int rn;
	
	private int startRow;
	private int endRow;
	private int page = 1;   // 기본 페이지 1
	private int size = 10;  //기본페이지 크기 10
	
	private int totalCount;
	private int totalPages;
	private String sortField = "CREATE_DT";
    private String sortOrder = "DESC";
	
	
    
	private int writingId;
	private String writingTitle;
	private String writingContent;
	private int writingViewCount;
	
	private String writingSortation;
	private String writingCategory;;

	private List<MultipartFile> files; //입력할 때 사용
	private List<PostFile> postFiles; //조회할 때 사용
	private List<Comment> comments;

	private String remainingFileIds;
	


    
}