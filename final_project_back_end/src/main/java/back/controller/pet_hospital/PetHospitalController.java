package back.controller.pet_hospital;

import back.model.common.CustomUserDetails;
import back.model.pet_hospital.PetHospital;
import back.service.pet_hospital.PetHospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/petHospital")
@RequiredArgsConstructor
@Slf4j
public class PetHospitalController {

    private final PetHospitalService petHospitalService;

    @PostMapping(
	  value = "/petHospital.do",
	  consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	public ResponseEntity<?> insertPetHospital(
			@RequestParam("animalId") int animalId,
		    @RequestParam("animalVisitDate") String animalVisitDate,
		    @RequestParam("animalHospitalName") String animalHospitalName,
		    @RequestParam("animalMedication") String animalMedication,
		    @RequestParam("animalTreatmentType") String animalTreatmentType,
		    @RequestParam("animalTreatmentMemo") String animalTreatmentMemo,
		    @RequestParam(value = "animalHospitalTreatmentId", required = false) Long animalHospitalTreatmentId,
		    @AuthenticationPrincipal CustomUserDetails userDetails
	) {
	    try {
	        PetHospital petHospital = new PetHospital();
	        petHospital.setAnimalId(animalId);
	        petHospital.setAnimalVisitDate(animalVisitDate);
	        petHospital.setAnimalHospitalName(animalHospitalName);
	        petHospital.setAnimalMedication(animalMedication);
	        petHospital.setAnimalTreatmentMemo(animalTreatmentMemo);
	        petHospital.setAnimalTreatmentType(animalTreatmentType);
	        petHospital.setCreateId(userDetails.getUsername());

	        PetHospital saved = petHospitalService.registerHospitalRecord(petHospital);

	        return ResponseEntity.ok().body(
	            Map.of(
	                "message", "등록 성공",
	                "animalHospitalTreatmentId", saved.getAnimalHospitalTreatmentId()
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
            List<PetHospital> list = petHospitalService.getAllByCreateDtDesc();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("조회 실패: " + e.getMessage());
        }
    }
    
    @PostMapping(value = "/update.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePetHospital(
        @RequestParam("animalHospitalTreatmentId") int id,
        @RequestParam("animalId") int animalId,
        @RequestParam("animalVisitDate") String animalVisitDate,
        @RequestParam("animalHospitalName") String animalHospitalName,
        @RequestParam("animalMedication") String animalMedication,
        @RequestParam("animalTreatmentType") String animalTreatmentType,
        @RequestParam("animalTreatmentMemo") String animalTreatmentMemo,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        PetHospital petHospital = new PetHospital();
        petHospital.setAnimalHospitalTreatmentId(id);
        petHospital.setAnimalId(animalId);
        petHospital.setAnimalVisitDate(animalVisitDate);
        petHospital.setAnimalHospitalName(animalHospitalName);
        petHospital.setAnimalMedication(animalMedication);
        petHospital.setAnimalTreatmentType(animalTreatmentType);
        petHospital.setAnimalTreatmentMemo(animalTreatmentMemo);

        petHospitalService.updatePetHospital(id, petHospital, userDetails);

        // 수정된 데이터 다시 반환
        return ResponseEntity.ok(petHospital);
    }
    
    @PostMapping("/delete.do")
    public ResponseEntity<?> deletePetHospital(
            @RequestBody Map<String, Long> payload,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long treatmentId = payload.get("animalHospitalTreatmentId");

        if (treatmentId == null) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다. ID가 없습니다.");
        }

        try {
        	petHospitalService.deletePetHospital(treatmentId, userDetails.getUsername());
            return ResponseEntity.ok().body(Map.of("message", "삭제 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("삭제 실패: " + e.getMessage());
        }
    }
} // 클래스 닫기