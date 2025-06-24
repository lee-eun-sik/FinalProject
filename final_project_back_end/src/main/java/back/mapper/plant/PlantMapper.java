package back.mapper.plant;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import back.model.alarm.Alarm;
import back.model.diary.Diary;
import back.model.pet.Pet;
import back.model.plant.Plant;

@Mapper
public interface PlantMapper {
	// 식물 목록 리스트
	public List<Plant> getPlantList(Plant plant);
	
	// 식물 정보 조회
	public List<Plant> plantInfo(Plant plant);
	
	//식물 병충해 로그 개별 수정
	public int updatePestLogs(Plant plant);
	
	//식물 병충해 로그 개별 삭제
	public int deletePestLogs(Plant plant);
		
	//식물 병충해 조회
	public List<Plant> pestlogs(Plant plant);
	
	//식물 병충해 저장
	public int savePestInfo(Plant plant);
	public int updatePestFileId(Plant plant);

	//식물 분갈이 개별 수정
	public int updatePlantRepottingLogs(Plant plant);
		
	//식물 분갈이 단건 조회
	public int getPlantRepottingLogsId(Plant plant);
	
	//식물 분갈이 로그 개별 삭제
	public int deletePlantRepottingLogs(Plant plant);
	
	//식물 분갈이 조회
	public List<Plant> repottinglogs(Plant plant);
	
	//식물 분갈이 저장
	public int saveRepottingInfo(Plant plant);
	
	//식물 일조량 단건 조회
	public int getPlantSunlightLogsId(Plant plant);
	
	//식물 일조량 개별 수정
	public int updatePlantSunlightLogs(Plant plant);
	
	//식물 일조량 개별 삭제
	public int deletePlantSunlightLogs(Plant plant);
	
	//식물 일조량 조회
	public List<Plant> findByPlantId(Plant plant);
	
	public List<Map<String, Object>> selectPlantCheck(int plant_id);
	
	
	//식물 일조량 저장
	public int saveSunlightInfo(Plant plant);
	
	public Plant selectPlantById(String plantId);
	
	//식물 등록
	public int create(Plant plant);
	public int updateFileId(@Param("plantId") Integer plantId, @Param("fileId") Integer fileId);

    // 식물 수정
    public int updatePlant(Plant plant);

    // 식물 삭제
    public int deletePlant(Plant plant);
    
    // 물주기 저장
    public int WaterCreate(Plant plant);
    
    // 물주기 삭제
    public int WaterDelete(Plant plant);
    
    // 물주기 리스트조회
    public List<Plant> waterList(Plant plant);
    
    public int updatePlantFileId(@Param("fileId") Long fileId, @Param("plantId") int plantId);
    
    public List<Plant> plantIdList(Plant plant);
}
