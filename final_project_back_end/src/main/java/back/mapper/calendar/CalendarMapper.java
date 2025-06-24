package back.mapper.calendar;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import back.model.calendar.Calendar;

@Mapper
public interface CalendarMapper {
	public List<Calendar> selectCountByDate(Calendar calendar);//달력 점
	public List <Calendar> selectLogsByDate(Calendar calendar);//달력 로그
	List<Map<String, Object>> selectUserAnimals(String usersId);//유저 동물 목록
	List<Map<String, Object>> selectUserPlants(String usersId); //유저 식물 몰록
}
