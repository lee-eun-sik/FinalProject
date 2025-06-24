package back.controller.pet_training_and_action;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import back.controller.pet_hospital.PetHospitalController;
import back.model.common.CustomUserDetails;
import back.model.pet_training_and_action.PetTrainingAndAction;
import back.service.pet_hospital.PetHospitalService;
import back.service.pet_training_and_action.PetTrainingAndActionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("/api/petTrainingAndAction")
@RequiredArgsConstructor
@Slf4j
public class PetTrainingAndActionController {
	private final PetTrainingAndActionService petTrainingAndActionService;
	
	@PostMapping(
	  value = "/petTrainingAndAction.do",
	  consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	public ResponseEntity<?> insertPetHospital(
			@RequestParam("animalId") int animalId,
		    @RequestParam("animalRecordDate") String animalRecordDate,
		    @RequestParam("animalTrainingType") String animalTrainingType,
		    @RequestParam("animalTrainingMemo") String animalTrainingMemo,
		    @RequestParam(value = "animalTrainingAction", required = false) Long animalTrainingAction,
		    @AuthenticationPrincipal CustomUserDetails userDetails
	) {
	    try {
	        PetTrainingAndAction petTrainingAndAction = new PetTrainingAndAction();
	        petTrainingAndAction.setAnimalId(animalId);
	        petTrainingAndAction.setAnimalRecordDate(animalRecordDate);
	        petTrainingAndAction.setAnimalTrainingMemo(animalTrainingMemo);
	        petTrainingAndAction.setAnimalTrainingType(animalTrainingType);
	        petTrainingAndAction.setCreateId(userDetails.getUsername());

	        PetTrainingAndAction saved = petTrainingAndActionService.registerTrainingAndActionRecord(petTrainingAndAction);

	        return ResponseEntity.ok().body(
	            Map.of(
	                "message", "등록 성공",
	                "animalTrainingAction", saved.getAnimalTrainingAction()
	            )
	        );
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("등록 실패: " + e.getMessage());
	    }
	}

    // 추후에 진료 조회, 수정, 삭제 메서드도 이 구조에 맞게 추가 가능
    @GetMapping("/list.do")
    public ResponseEntity<?> getAllRecordsByCreateDt() {
        try {
            List<PetTrainingAndAction> list = petTrainingAndActionService.getAllByCreateDtDesc();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("조회 실패: " + e.getMessage());
        }
    }
    
    @PostMapping(value = "/update.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePetHospital(
        @RequestParam("animalTrainingAction") int id,
        @RequestParam("animalId") int animalId,
        @RequestParam("animalRecordDate") String animalRecordDate,
        @RequestParam("animalTrainingType") String animalTrainingType,
        @RequestParam("animalTrainingMemo") String animalTrainingMemo,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
    	PetTrainingAndAction petTrainingAndAction = new PetTrainingAndAction();
    	petTrainingAndAction.setAnimalTrainingAction(id);
    	petTrainingAndAction.setAnimalId(animalId);
    	petTrainingAndAction.setAnimalRecordDate(animalRecordDate);
    	petTrainingAndAction.setAnimalTrainingType(animalTrainingType);
    	petTrainingAndAction.setAnimalTrainingMemo(animalTrainingMemo);

        petTrainingAndActionService.updatePetTrainingAndAction(id, petTrainingAndAction, userDetails);

        // 수정된 데이터 다시 반환
        return ResponseEntity.ok(petTrainingAndAction);
    }
    
    @PostMapping("/delete.do")
    public ResponseEntity<?> deletePetHospital(
            @RequestBody Map<String, Long> payload,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long trainingAndActionId = payload.get("animalTrainingAction");

        if (trainingAndActionId == null) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다. ID가 없습니다.");
        }

        try {
        	petTrainingAndActionService.deletePetTrainingAndAction(trainingAndActionId, userDetails.getUsername());
            return ResponseEntity.ok().body(Map.of("message", "삭제 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("삭제 실패: " + e.getMessage());
        }
    }
}
