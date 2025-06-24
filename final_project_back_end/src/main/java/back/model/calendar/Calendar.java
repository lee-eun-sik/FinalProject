package back.model.calendar;

import back.model.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자 자동 생성
@EqualsAndHashCode(callSuper = true)
public class Calendar extends Model{
	private String logDate;
	private String type;
	private int totalCount;

    // 날짜 조건 필터용
    private String year;
    private String month;
    private String day;

    private String name;
    private String category;
    private Integer id;
    private String time;
    

}
