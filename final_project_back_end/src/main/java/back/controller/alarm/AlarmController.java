package back.controller.alarm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import back.model.alarm.Alarm;
import back.model.combo.Combo;
import back.model.common.CustomUserDetails;
import back.model.user.User;
import back.model.pet.Pet;
import back.model.plant.Plant;
import back.service.alarm.AlarmService;
import back.service.pet.PetService;
import back.service.plant.PlantService;
import back.util.ApiResponse;
import back.util.SecurityUtil;


@RestController
@RequestMapping("/api/alarm")
@Slf4j
public class AlarmController{
	
@Autowired
private AlarmService alarmService;

@Autowired
private PetService petService;

@Autowired
private PlantService plantService;


@PostMapping("/list.do")
public ResponseEntity<?> getAlarmList(@RequestBody Alarm alarm) {
	log.info("alarmPetId : {}", alarm.getPetId());
	List<Alarm> alarmList = alarmService.getList(alarm);

	return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", alarmList));
}

@PostMapping("/oneList.do")
public ResponseEntity<?> getOneAlarmList(@RequestBody Alarm alarm) {
	log.info("petId, 카테고리 : 1111111111", alarm.getPetId()+ " / " + alarm.getCategory());
	List alarmList = alarmService.getOneList(alarm);

	return ResponseEntity.ok(new ApiResponse<>(true, "목록 조회 성공", alarmList));
}

@PostMapping("/create.do")
public ResponseEntity<?> createAlarm (@RequestBody Alarm alarm) {
	CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
		
		SecurityUtil.checkAuthorization(userDetails);
		alarm.setUsersId(userDetails.getUser().getUsersId());
		boolean isCreated = alarmService.create(alarm);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "알람 등록 성공" : "알람 등록 실패", null));
}

@PostMapping("/update.do")
public ResponseEntity<?> updateAlarm (@RequestBody Alarm alarm) {
	CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
		
		SecurityUtil.checkAuthorization(userDetails);
		alarm.setUsersId(userDetails.getUser().getUsersId());
		boolean isCreated = alarmService.update(alarm);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "알람 수정 성공" : "알람 수정 실패", null));
}


@PostMapping("/AllUpdate.do")
public ResponseEntity<?> AllupdateAlarm (@RequestBody Alarm alarm) {
	CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
		
		SecurityUtil.checkAuthorization(userDetails);
		alarm.setUsersId(userDetails.getUser().getUsersId());
		boolean isCreated = alarmService.AllUpdate(alarm);
		return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "알람 수정 성공" : "알람 수정 실패", null));
}


@PostMapping("/delete.do")
public ResponseEntity<?> deleteAlarm (@RequestBody Alarm alarm) {
	CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
		
		SecurityUtil.checkAuthorization(userDetails);
		alarm.setUsersId(userDetails.getUser().getUsersId());
		boolean isDeleted = alarmService.delete(alarm);
		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "알람 수정 성공" : "알람 수정 실패", null));
}

@PostMapping("/petDelete.do")
public ResponseEntity<?> petDeleteAlarm (@RequestBody Alarm alarm) {
	CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
		
		SecurityUtil.checkAuthorization(userDetails);
		
		List<Alarm> Idlist = alarmService.alarmIdList(alarm);    // 알람아이디조회
		
		alarm.setUpdateId(userDetails.getUser().getUsersName());
		boolean isDeleted = alarmService.petDelete(alarm);    // 식물삭제, 동물삭제 같은서비스
		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "알람 삭제 성공" : "알람 삭제 실패", isDeleted ? Idlist : null));
}

@PostMapping("/plantDelete.do")
public ResponseEntity<?> plantDeleteAlarm (
		@RequestBody Alarm alarm,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		
		SecurityUtil.checkAuthorization(userDetails);
		
		List<Alarm> Idlist = alarmService.alarmIdList(alarm);    // 알람아이디조회
		
		alarm.setUpdateId(userDetails.getUser().getUsersName());
		boolean isDeleted = alarmService.petDelete(alarm);    // 식물삭제, 동물삭제 같은서비스
		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "알람 삭제 성공" : "알람 삭제 실패", isDeleted ? Idlist : null));
}

@PostMapping("/logoutDelete.do")
public ResponseEntity<?> logoutDeleteAlarm (
		@RequestBody Alarm alarm,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
	
		SecurityUtil.checkAuthorization(userDetails);
		
		List<Map<String, Object>> IdList = new ArrayList<>();
		
		Pet pet = new Pet();
		pet.setCreateId(userDetails.getUser().getUsersName());
		List<Pet> petList = petService.petIdList(pet);    // 동물아이디 조회  petId로
		
		for (Pet p : petList) {
			Map<String, Object> alarmMap = new HashMap<>(); // 루프마다 새 맵 생성
		    alarmMap.put("petId", p.getPetId());
		    alarmMap.put("category", "ANI");
		    IdList.add(alarmMap);
		}
		
		Plant plant = new Plant();
		plant.setCreateId(userDetails.getUser().getUsersName());
		List<Plant> plantList = plantService.plantIdList(plant);    // 식물아이디 조회  petId로
		for (Plant pl : plantList) {
			Map<String, Object> alarmMap = new HashMap<>(); // 루프마다 새 맵 생성
			alarmMap.put("petId", pl.getPetId());
			alarmMap.put("category", "PLA");
			IdList.add(alarmMap);
		}

		log.info("IdList size: {}", IdList.size());
		
		alarm.setUpdateId(userDetails.getUser().getUsersName());
		alarm.setIdList(IdList);   // 조회해온 아이디리스트 alarm에 추가
		boolean isDeleted = false;
		List<Alarm> list = new ArrayList<>();
		try {
			list = alarmService.logoutDelete(alarm);    // 식물삭제, 동물삭제 같은서비스
			isDeleted = true;
		} catch (Exception e) {
			log.info("알람 수정시 오류 Controller");
		}
		
		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "알람 수정 성공" : "알람 수정 실패", isDeleted ? list : null));
}


@PostMapping("/dropDelete.do")
public ResponseEntity<?> dropDeleteAlarm (
		@RequestBody Alarm alarm,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
	
		SecurityUtil.checkAuthorization(userDetails);
		
		List<Map<String, Object>> IdList = new ArrayList<>();
		
		Pet pet = new Pet();
		pet.setCreateId(userDetails.getUser().getUsersName());
		List<Pet> petList = petService.petIdList(pet);    // 동물아이디 조회  petId로
		
		for (Pet p : petList) {
			Map<String, Object> alarmMap = new HashMap<>(); // 루프마다 새 맵 생성
		    alarmMap.put("petId", p.getPetId());
		    alarmMap.put("category", "ANI");
		    IdList.add(alarmMap);
		}
		
		Plant plant = new Plant();
		plant.setCreateId(userDetails.getUser().getUsersName());
		List<Plant> plantList = plantService.plantIdList(plant);    // 식물아이디 조회  petId로
		for (Plant pl : plantList) {
			Map<String, Object> alarmMap = new HashMap<>(); // 루프마다 새 맵 생성
			alarmMap.put("petId", pl.getPetId());
			alarmMap.put("category", "PLA");
			IdList.add(alarmMap);
		}

		log.info("IdList size: {}", IdList.size());
		
		alarm.setUpdateId(userDetails.getUser().getUsersName());
		alarm.setIdList(IdList);   // 조회해온 아이디리스트 alarm에 추가
		boolean isDeleted = false;
		List<Alarm> list = new ArrayList<>();
		try {
			list = alarmService.dropDeleteAlarm(alarm);    // 식물삭제, 동물삭제 같은서비스
			isDeleted = true;
		} catch (Exception e) {
			log.info("알람 수정시 오류 Controller");
		}
		
		return ResponseEntity.ok(new ApiResponse<>(isDeleted, isDeleted ? "알람 삭제 성공" : "알람 삭제 실패", isDeleted ? list : null));
}


}