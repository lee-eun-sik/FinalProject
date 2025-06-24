package back.controller.pet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import back.model.combo.Combo;
import back.model.common.CustomUserDetails;

import back.model.common.PostFile;

import back.model.diary.Diary;

import back.model.pet.Pet;

import back.service.file.FileService;

import back.model.pet_hospital.PetHospital;

import back.service.pet.PetService;
import back.util.ApiResponse;
import back.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pet")
@Slf4j
public class PetController {

    @Autowired
    private PetService petService;
    @Autowired
    private FileService fileService;
    @PostMapping(value = "/animalregister.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerPet(
            @RequestPart("data") Pet pet,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 사용자 인증 확인
        SecurityUtil.checkAuthorization(userDetails);

        // 사용자 및 시간 정보 설정
        pet.setCreateId(userDetails.getUsername());
        pet.setUsersId(userDetails.getUsername());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        pet.setCreateDt(LocalDateTime.now().format(formatter));

        // 이미지 파일이 있다면 설정
        if (imageFile != null && !imageFile.isEmpty()) {
            pet.setFiles(List.of(imageFile));
        }

        // 서비스 호출
        boolean isCreated = petService.registerPet(pet);

        return ResponseEntity.ok(new ApiResponse<>(isCreated, isCreated ? "반려동물 등록 성공" : "반려동물 등록 실패", null));
    }
    
    @PostMapping(value = "/petUpdate.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePet(
		@ModelAttribute Pet pet,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        SecurityUtil.checkAuthorization(userDetails);
        log.info("petId : {}",pet.getAnimalId());

        pet.setUpdateId(userDetails.getUsername());
        

        if (files != null && !files.isEmpty()) {
            pet.setFiles(files);
        }

        boolean updated = petService.updatePet(pet);
        return ResponseEntity.ok(new ApiResponse<>(updated, updated ? "반려동물 수정 성공" : "반려동물 수정 실패", null));
    }
    
    @PostMapping("/petDelete.do")
    public ResponseEntity<?> deletePet(
        @RequestParam("animalId") Integer animalId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        SecurityUtil.checkAuthorization(userDetails);

        boolean deleted = petService.deletePet(animalId, userDetails.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(deleted, deleted ? "반려동물 삭제 성공" : "반려동물 삭제 실패", null));
    }
    
    @GetMapping("/getPetById.do")
    public ResponseEntity<?> getPetById(

        @RequestParam("animalId") Integer animalId,

        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        SecurityUtil.checkAuthorization(userDetails);

        if (animalId == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "animalId는 필수입니다.", null));
        }

        String category = "ANI";
        Pet pet = petService.getPetById(animalId, userDetails.getUsername(), category);
        if (pet == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, "반려동물 조회 실패", null));
        }
    
   
        System.out.println("fileUrl from DB: " + pet.getFileUrl());

        return ResponseEntity.ok(new ApiResponse<>(true, "반려동물 조회 성공", pet));
    }
    
    
}