package back.model.alarm;

import java.util.List;
import java.util.Map;

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
public class Alarm extends Model {
	
	private int alarmId;
	private int petId;
	private String alarmName;
	
	private String alarmCycle;
	
	private String alarmTime;
	private int hour;
	private int min;
	
	private String startDate;
	private int year;
	private String month;
	private int day;
	private int monthNum;
	
	private String activeYn;      // 사용 여부 (Y/N)
	
	private String category;    // 동/식물 구분 카테고리
	private String type;		// 먹이 타입  사료 간식 영양제 약
	
	private List<Map<String, Object>> IdList;    //  아이디리스트
	
}