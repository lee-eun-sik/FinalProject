package back.controller.plant;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import back.model.common.CustomUserDetails;
import back.model.diary.Diary;
import back.model.plant.Plant;
import back.service.plant.PlantService;
import back.util.ApiResponse;
import back.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/plant")
@Slf4j
public class PlantController {
	@Autowired
	private PlantService plantService;
	//
	// 식물 목록 리스트
	@PostMapping("/plant-list.do")
	public ResponseEntity<?> getPlantList(@RequestBody Plant plant){
	    log.info(plant.toString());
	    List<Plant> diaryList= plantService.getPlantList(plant);
	    Map<String, Object> dataMap = new HashMap();
	    dataMap.put("list", diaryList);
	    dataMap.put("plant",plant);
	    return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", dataMap));
	}

	
	
	// 식물 정보 조회
	@PostMapping("/plant-info.do")
	public ResponseEntity<?> plantInfo(@RequestBody Plant plant) {
		List plantSunList = plantService.plantInfo(plant);
		return ResponseEntity.ok(new ApiResponse<>(true, "식물 정보 조회 성공", plantSunList));
	}

	// 식물 병충해 로그 개별 수정
	@PostMapping("/pest-update.do")
	public ResponseEntity<?> updateDiary(@ModelAttribute Plant plant,
			@RequestPart(value = "files", required = false) List<MultipartFile> files,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		SecurityUtil.checkAuthorization(userDetails);
		plant.setUpdateId(userDetails.getUsername());
		plant.setFiles(files);
		boolean isUpdated = plantService.updatePestLogs(plant);
		return ResponseEntity.ok(new ApiResponse<>(isUpdated, isUpdated ? "게시글 수정 성공" : "게시글 수정 실패", null));
	}

	// 식물 병충해 로그 개별 삭제
	@PostMapping("/pest-delete.do")
	public ResponseEntity<?> deletePestLog(@RequestBody Plant plant,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		plant.setUpdateId(userDetails.getUsername());

		boolean isDeleted = plantService.deletePestLogs(plant);

		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "일지 삭제 성공" : "일지 삭제 실패", null));
	}

	// 식물 병충해 조회
	@PostMapping("/pest-logs.do")
	public ResponseEntity<?> pestLogs(@RequestBody Plant plant) {
		List plantSunList = plantService.pestlogs(plant);
		return ResponseEntity.ok(new ApiResponse<>(true, "분갈이 조회 성공", plantSunList));
	}

	// 식물 병충해 저장
	@PostMapping("/pest-save.do")
	public ResponseEntity<?> savePestInfo(@ModelAttribute Plant plant,
			@RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {
		// 인증 및 권한 체크
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);

		plant.setCreateId(userDetails.getUsername());
		plant.setFiles(files);

		// 서비스 호출할 때 파일 처리 포함
		boolean isCreated = plantService.savePestInfo(plant);

		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "저장성공" : "저장실패", null));
	}

	// 식물 분갈이 로그 개별 수정
	@PostMapping("/repotting-update.do")
	public ResponseEntity<?> updatePlantRepottingLogs(@RequestBody Plant plant,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		plant.setUpdateId(userDetails.getUsername());

		boolean isUpdate = plantService.updatePlantRepottingLogs(plant);

		return ResponseEntity.ok(new ApiResponse<>(isUpdate, isUpdate ? "일지 수정 성공" : "일지 수정 실패", null));
	}

	// 식물 분갈이 단건 조회
	@PostMapping("/repotting-blist.do")
	public ResponseEntity<?> getPlantRepottingLogsId(@RequestBody Plant plant,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		plant.setUpdateId(userDetails.getUsername());

		boolean isResult = plantService.getPlantRepottingLogsId(plant);

		return ResponseEntity.ok(new ApiResponse<>(isResult, isResult ? "일지 조회 성공" : "일지 조회 실패", null));
	}

	// 식물 분갈이 로그 개별 삭제
	@PostMapping("/repotting-delete.do")
	public ResponseEntity<?> deleteRepottingLog(@RequestBody Plant plant,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		plant.setUpdateId(userDetails.getUsername());

		boolean isDeleted = plantService.deletePlantRepottingLogs(plant);

		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "일지 삭제 성공" : "일지 삭제 실패", null));
	}

	// 식물 분갈이 조회
	@PostMapping("/repotting-logs.do")
	public ResponseEntity<?> repottingLogs(@RequestBody Plant plant) {
		List plantSunList = plantService.repottinglogs(plant);
		return ResponseEntity.ok(new ApiResponse<>(true, "분갈이 조회 성공", plantSunList));
	}

	// 식물 분갈이 저장
	@PostMapping("/repotting-save.do")
	public ResponseEntity<?> saveRepottingInfo(@RequestBody Plant plant) throws IOException {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);
		plant.setCreateId(userDetails.getUsername());

		boolean isCreated = plantService.saveRepottingInfo(plant);

		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "저장성공" : "저장실패", null));
	}

	// 식물 일조량 로그 단건 조회
	@PostMapping("/sunlight-alist.do")
	public ResponseEntity<?> getPlantSunlightLogsId(@RequestBody Plant plant,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		plant.setUpdateId(userDetails.getUsername());

		boolean isResult = plantService.getPlantSunlightLogsId(plant);

		return ResponseEntity.ok(new ApiResponse<>(isResult, isResult ? "일지 조회 성공" : "일지 조회 실패", null));
	}

	// 식물 일조량 로그 개별 수정
	@PostMapping("/sunlight-update.do")
	public ResponseEntity<?> updatePlantSunlightLogs(@RequestBody Plant plant,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		plant.setUpdateId(userDetails.getUsername());

		boolean isUpdate = plantService.updatePlantSunlightLogs(plant);

		return ResponseEntity.ok(new ApiResponse<>(isUpdate, isUpdate ? "일지 수정 성공" : "일지 수정 실패", null));
	}

	// 식물 일조량 로그 개별 삭제
	@PostMapping("/sunlight-delete.do")
	public ResponseEntity<?> deleteSunlightLog(@RequestBody Plant plant,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		plant.setUpdateId(userDetails.getUsername());

		boolean isDeleted = plantService.deletePlantSunlightLogs(plant);

		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "일지 삭제 성공" : "일지 삭제 실패", null));
	}

	// 식물 일조량 조회
	@PostMapping("/sunlight-logs.do")
	public ResponseEntity<?> getSunlightLogs(@RequestBody Plant plant) {
		log.info("plantplantplant" + plant.getPlantId());
		List plantSunList = plantService.findByPlantId(plant);
		return ResponseEntity.ok(new ApiResponse<>(true, "간단 식물 목록 조회 성공", plantSunList));
	}

	// 식물이름 + 입수일만 조회
	@PostMapping("/simple-list.do")
	public ResponseEntity<?> getSimplePlantList(@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);

		Integer plantId = null;

		List<Map<String, Object>> simpleList = plantService.getPlantCheck(plantId);

		Map<String, Object> data = new HashMap<>();
		data.put("list", simpleList);

		return ResponseEntity.ok(new ApiResponse<>(true, "간단 식물 목록 조회 성공", data));
	}

	// 식물 일조량 저장
	@PostMapping("/sunlight-save.do")
	public ResponseEntity<?> saveSunlightInfo(@RequestBody Plant plant) throws IOException {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		SecurityUtil.checkAuthorization(userDetails);
		plant.setCreateId(userDetails.getUsername());

		boolean isCreated = plantService.saveSunlightInfo(plant);

		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "저장성공" : "저장실패", null));
	}

	// 식물 상세 조회
	@GetMapping("/{plantId}")
	public ResponseEntity<?> getPlantById(@PathVariable String plantId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		Plant plant = plantService.getPlantById(plantId);

		if (plant == null) {
			return ResponseEntity.status(404).body(new ApiResponse<>(false, "식물을 찾을 수 없습니다.", null));
		}

		// 본인의 식물만 조회 가능하도록 제한
		if (!plant.getUsersId().equals(userDetails.getUsername())) {
			return ResponseEntity.status(403).body(new ApiResponse<>(false, "권한이 없습니다.", null));
		}

		return ResponseEntity.ok(new ApiResponse<>(true, "식물 조회 성공", plant));
	}

	// 식물 등록 (파일 업로드 포함)
	@PostMapping(value = "/create.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> create(@ModelAttribute Plant plant,
			@RequestPart(value = "files", required = false) List<MultipartFile> files,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		plant.setUsersId(userDetails.getUsername());
		plant.setCreateId(userDetails.getUsername());
		plant.setFiles(files);
		log.info("파일 개수: {}", files != null ? files.size() : 0);
		boolean isCreated = plantService.create(plant);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "식물 등록 성공" : "식물 등록 실패", null));
	}

	// 식물 수정 (파일 업로드 포함)
	@PostMapping(value = "/updatePlant.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> update(@ModelAttribute Plant plant,
			@RequestPart(value = "files", required = false) List<MultipartFile> files, // 복수 파일 받기
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		SecurityUtil.checkAuthorization(userDetails);
		plant.setUpdateId(userDetails.getUsername());
		plant.setFiles(files); // files를 plant에 세팅
		boolean isUpdated = plantService.updatePlant(plant);
		return ResponseEntity.ok(new ApiResponse<>(isUpdated, isUpdated ? "게시글 수정 성공" : "게시글 수정 실패", null));
	}

	// 식물 삭제
	@PostMapping("/deletePlant.do")
	public ResponseEntity<?> deletePlant(@RequestBody Plant plant,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		SecurityUtil.checkAuthorization(userDetails);
		log.info("plant:" , plant.getPlantId());
		plant.setUpdateId(userDetails.getUsername());
		boolean isDeleted = plantService.deletePlant(plant);
		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "게시물 삭제 성공" : "게시물 삭제 실패", null));
	}
	
	@PostMapping("/waterCreate.do")
	public ResponseEntity<?> WaterCreate(
			@RequestBody Plant plant, @AuthenticationPrincipal CustomUserDetails userDetails)
					throws NumberFormatException, IOException {
		
		log.info("watercreate plantId: {}", plant.getPlantId());
		SecurityUtil.checkAuthorization(userDetails);
				
		plant.setCreateId(userDetails.getUser().getUsersId());
		boolean isCreated = plantService.WaterCreate(plant);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "식물 줄주기 성공" : "식물 물주기 실패", plant));
		
	}
	
	@PostMapping("/waterDelete.do")
	public ResponseEntity<?> WaterDelete(
			@RequestBody Plant plant, @AuthenticationPrincipal CustomUserDetails userDetails)
					throws NumberFormatException, IOException {
		
		log.info("watercreate waterId: {}", plant.getWaterId());
		SecurityUtil.checkAuthorization(userDetails);
				
		plant.setCreateId(userDetails.getUser().getUsersId());
		boolean isCreated = plantService.WaterDelete(plant);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "식물 물주기삭제 성공" : "식물 물주기삭제 실패", plant));
		
	}
	
	@PostMapping("/waterList.do")
    public List<Plant> waterList(@RequestBody Plant plant,  @AuthenticationPrincipal CustomUserDetails userDetails)
    				throws NumberFormatException, IOException {
		
		log.info("waterlist plantId: {}", plant.getPlantId());
		SecurityUtil.checkAuthorization(userDetails);
		plant.setCreateId(userDetails.getUsername());
		List<Plant> waterList = plantService.waterList(plant);

		
	    return waterList;
    }
}
