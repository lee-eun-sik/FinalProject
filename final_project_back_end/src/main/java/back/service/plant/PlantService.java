package back.service.plant;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import back.model.alarm.Alarm;
import back.model.pet.Pet;
import back.model.plant.Plant;

public interface PlantService {
	
	// 식물 목록 리스트
	public List<Plant> getPlantList(Plant plant);
	
	// 식물 정보 조회
	public List<Plant> plantInfo(Plant plant);
	
	//식물 병충해 로그 개별 수정
	public boolean updatePestLogs(Plant plant);
		
	// 식물 병충해 로그 개별 삭제
	public boolean deletePestLogs(Plant plant);
	
	//식물 병충해 조회
	public List<Plant> pestlogs(Plant plant);
		
	//식물 병충해 저장
	public boolean savePestInfo(Plant plant);
	
	//식물 분갈이 개별 수정
	public boolean updatePlantRepottingLogs(Plant plant);
		
	//식물 분갈이 단건 조회
	public boolean getPlantRepottingLogsId(Plant plant);
		
	//식물 분갈이 로그 개별 삭제
	public boolean deletePlantRepottingLogs(Plant plant);
		
	//식물 분갈이 조회
	public List<Plant> repottinglogs(Plant plant);
	
	//식물 분갈이 저장
	public boolean saveRepottingInfo(Plant plant);
	
	//식물 일조량 단건 조회
	public boolean getPlantSunlightLogsId(Plant plant);
	
	//식물 일조량 개별 수정
	public boolean updatePlantSunlightLogs(Plant plant);
	
	//식물 일조량 개별 삭제
	public boolean deletePlantSunlightLogs(Plant plant);
	
	//식물 일조량 조회
	public List<Plant> findByPlantId(Plant plant);
	
	public List<Map<String, Object>> getPlantCheck(Integer plantId);
	
	//식물 일조량 저장
	public boolean saveSunlightingRecord(Plant plant);
    
	//식물 저장
	public boolean saveSunlightInfo(Plant plant);
	
	public Plant getPlantById(String plantId) throws HException;
	
    // 식물 등록
    public boolean create(Plant plant) throws HException;

    // 식물 수정
    public boolean updatePlant(Plant plant) throws HException;

    // 식물 삭제
    public boolean deletePlant(Plant plant) throws HException;  
    
    //물주기 저장
    public boolean WaterCreate(Plant plant) throws NumberFormatException, IOException;
    
    //물주기 삭제
    public boolean WaterDelete(Plant plant) throws NumberFormatException, IOException;
    
    //물주기조회
    public List<Plant> waterList(Plant plant) throws NumberFormatException, IOException;
    
    // 식물아이디조회
    public List<Plant> plantIdList(Plant plant);
    
}
