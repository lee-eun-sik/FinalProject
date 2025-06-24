package back.service.calendar;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import back.exception.HException;
import back.mapper.calendar.CalendarMapper;
import back.model.calendar.Calendar;
import back.service.diary.DiaryServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CalendarServiceImpl implements CalendarService {
	@Autowired
    private CalendarMapper calendarMapper;

	@Override
	public List<Calendar> selectCountByDate(Calendar calendar) {
		try {
		return calendarMapper.selectCountByDate(calendar);
		}catch(Exception e) {
			log.error("점 조회 실패", e);
			throw new HException("점 조회 실패",e);
		}
	}

	@Override
	public List<Calendar> selectLogsByDate(Calendar calendar) {
		try {
			if (calendar.getLogDate() == null &&
		            calendar.getYear() != null &&
		            calendar.getMonth() != null &&
		            calendar.getDay() != null) {

		            String logDate = String.format("%04d-%02d-%02d",
		                    Integer.parseInt(calendar.getYear()),
		                    Integer.parseInt(calendar.getMonth()),
		                    Integer.parseInt(calendar.getDay()));
		            calendar.setLogDate(logDate);
		        }
		return calendarMapper.selectLogsByDate(calendar);
		
		}catch(Exception e) {
			log.error("로그 조회 실패", e);
			throw new HException("로그 조회 실패",e);
		}
	}
	@Override
	public List<Map<String, Object>> selectUserAnimals(String usersId) {
		return calendarMapper.selectUserAnimals(usersId);
	}

	@Override
	public List<Map<String, Object>> selectUserPlants(String usersId) {
		return calendarMapper.selectUserPlants(usersId);
	}
}
