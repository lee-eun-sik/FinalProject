package back.service.calendar;

import java.util.List;
import java.util.Map;

import back.model.calendar.Calendar;

public interface CalendarService {
	public List<Calendar> selectCountByDate(Calendar calendar);
	public List<Calendar> selectLogsByDate(Calendar calendar);
	public List<Map<String, Object>> selectUserAnimals(String usersId);
	public List<Map<String, Object>> selectUserPlants(String usersId);
}
