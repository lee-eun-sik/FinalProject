package back.model.home;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import back.model.Model;
import back.model.common.PostFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // 기본 생성자 자동 생성
@AllArgsConstructor  // 모든 필드를 매개변수로 하는 생성사 자동 생성
@EqualsAndHashCode(callSuper = true)
public class Home extends Model {
	
    private int fileId;

    // 동물 관련 컬럼 (ANIMAL 테이블에만 존재)
    private String animalId;
    private String animalName;
    private String animalSpecies;
    private String animalAdoptionDate; 
    private String birthDate; 
    private String gender;
    private String animalMemo;

    // 식물 관련 컬럼 (PLANT 테이블에만 존재)
    private String plantId;
    private String plantName;
    private String plantType;
    private String plantPurchaseDate; 
    private String plantSunPreference;
    private String plantGrowStatus;
    
    private String sortField; // 정렬 기준 컬럼명 (예: "ANIMAL_NAME", "CREATE_DT")
    private String sortOrder; // 정렬 순서 (예: "ASC", "DESC")\
    
 	private List<MultipartFile> files; //입력할 때 사용
	private List<PostFile> postFiles; //조회할 때 사용
	private String remainingFileIds;
    
}