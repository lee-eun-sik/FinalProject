package back.mapper.alarm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.alarm.Alarm;


@Mapper
public interface AlarmMapper {
	
	public List<Alarm> OneList(Alarm alarm);
	
	public List<Alarm> list(Alarm alarm);
	
	public int create(Alarm alarm);
	
	public int update(Alarm alarm);
	
	public int AllUpdate(Alarm alarm);
	
	public int delete(Alarm alarm);
	
	public int petDelete(Alarm alarm);
	
	public List<Alarm> alarmIdList(Alarm alarm);
	
	public int logoutDelete(Alarm alarm);
	
	public int dropDeleteAlarm(Alarm alarm);
	
	
}
