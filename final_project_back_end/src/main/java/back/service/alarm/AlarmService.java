package back.service.alarm;

import java.util.List;

import back.model.alarm.Alarm;


public interface AlarmService {
	
	public List getOneList(Alarm alarm);
    
    public List<Alarm> getList(Alarm alarm);
    
    public boolean create(Alarm alarm);
    
    public boolean update(Alarm alarm);
    
    public boolean delete(Alarm alarm);
    
    public boolean AllUpdate(Alarm alarm);
    
    public boolean petDelete(Alarm alarm);
    
    public List<Alarm> alarmIdList(Alarm alarm);
    
    public List<Alarm> logoutDelete(Alarm alarm);
    
    public List<Alarm> dropDeleteAlarm(Alarm alarm);

}