package back.service.alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import back.mapper.alarm.AlarmMapper;
import back.model.alarm.Alarm;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AlarmServiceImpl implements AlarmService {
	
    @Autowired
    private AlarmMapper alarmMapper;
	
    @Override
	public List getOneList(Alarm alarm) {
    	
    	return alarmMapper.OneList(alarm);
	}
    

	@Override
	public List<Alarm> getList(Alarm alarm) {
		log.info("list 호출!!!!!!!!");
		return alarmMapper.list(alarm);
	}



	@Override
	public boolean create(Alarm alarm) {
		boolean result = alarmMapper.create(alarm) > 0;
		return result;
	}


	
	@Override
	public boolean update(Alarm alarm) {
		boolean result = alarmMapper.update(alarm) > 0;
		return result;
	}
	
	@Override
	public boolean AllUpdate(Alarm alarm) {
		boolean result = alarmMapper.AllUpdate(alarm) > 0;
		return result;
	}
	
	@Override
	public boolean delete(Alarm alarm) {
		boolean result = alarmMapper.delete(alarm) > 0;
		return result;
	}


	@Override
	public boolean petDelete(Alarm alarm) {
		boolean result = alarmMapper.petDelete(alarm) > 0;
		return result;
	}


	@Override
	public List<Alarm> alarmIdList(Alarm alarm) {
		return alarmMapper.alarmIdList(alarm);
	}


	@Override
	public List<Alarm> logoutDelete(Alarm alarm) {

		List<Alarm> alarmIdList = new ArrayList<>();
		int result = 0;
		
		for (Map<String, Object> idMap : alarm.getIdList()) {
		    Integer petId = (Integer) idMap.get("petId");       // Object → Integer로 형변환
		    String category = (String) idMap.get("category");   // Object → String으로 형변환
		    log.info("petId: {}, category: {}", petId, category);
		    
		    try {
			    alarm.setPetId(petId);
			    alarm.setCategory(category);
		    
			    // xml에서 수정할때 수정전후가 값이 같을경우 1이아닌 0이 반환되기때문에 나눔
			    // 문제없으면 무조건 1 오르게   이후에 비교하기위함
		    	alarmMapper.logoutDelete(alarm);
		    	
		    	// 정상적으로 수정시 알람아이디 조회후 리스트에추가
		    	List<Alarm> alarmId = alarmMapper.alarmIdList(alarm);
		    	alarmIdList.addAll(alarmId);
					
		    	// 문제없을시 +1
		    	result++;
		    } catch(Exception e) {
		    	log.info("알람수정중 오류 발생", e);
		    }	
		}
		
		// 유저가 가지고있는 삭제되지않은 식물,동물 아이디 = 알람수정횟수 같지않으면 빈배열 반환
		if(alarm.getIdList().size() != result) {
			log.info("빈배열 반환");
			return new ArrayList<>();
		}	
		
		return alarmIdList;
	}


	@Override
	public List<Alarm> dropDeleteAlarm(Alarm alarm) {
		List<Alarm> alarmIdList = new ArrayList<>();
		int result = 0;
		
		for (Map<String, Object> idMap : alarm.getIdList()) {
		    Integer petId = (Integer) idMap.get("petId");       // Object → Integer로 형변환
		    String category = (String) idMap.get("category");   // Object → String으로 형변환
		    log.info("petId: {}, category: {}", petId, category);
		    
		    try {
			    alarm.setPetId(petId);
			    alarm.setCategory(category);
		    
			    // xml에서 수정할때 수정전후가 값이 같을경우 1이아닌 0이 반환되기때문에 나눔
			    // 문제없으면 무조건 1 오르게   이후에 비교하기위함
		    	alarmMapper.dropDeleteAlarm(alarm);
		    	
		    	// 정상적으로 수정시 알람아이디 조회후 리스트에추가
		    	List<Alarm> alarmId = alarmMapper.alarmIdList(alarm);
		    	alarmIdList.addAll(alarmId);
					
		    	// 문제없을시 +1
		    	result++;
		    } catch(Exception e) {
		    	log.info("알람수정중 오류 발생", e);
		    }	
		}
		
		// 유저가 가지고있는 삭제되지않은 식물,동물 아이디 = 알람수정횟수 같지않으면 빈배열 반환
		if(alarm.getIdList().size() != result) {
			log.info("빈배열 반환");
			return new ArrayList<>();
		}	
		
		return alarmIdList;
	}
    


}