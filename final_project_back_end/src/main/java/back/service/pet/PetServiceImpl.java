
package back.service.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import back.exception.HException;
import back.mapper.file.FileMapper;
import back.mapper.pet.PetMapper;
import back.mapper.pet_training_and_action.PetTrainingAndActionMapper;
import back.model.alarm.Alarm;
import back.model.common.PostFile;
import back.model.pet.Pet;
import back.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetMapper petMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private PetTrainingAndActionMapper petTrainingAndActionMapper;
    // 반려동물 등록 처리
    @Override
    @Transactional
    public boolean registerPet(Pet pet) {
        try {
            if (pet.getCreateDt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date parsedDate = sdf.parse(pet.getCreateDt());
                // 변환만 해두고 사용 안함
            }

            boolean result = petMapper.insertPet(pet) > 0;

            List<MultipartFile> files = pet.getFiles();
            int animalId = pet.getAnimalId();

            if (result && files != null && animalId > 0) {

                List<PostFile> fileList = FileUploadUtil.uploadFiles(
                    files,
                    "pet",
                    animalId,
                    "ANI", // 카테고리
                    pet.getCreateId()
                );

                for (PostFile postFile : fileList) {
                    boolean insertResult = fileMapper.insertFile(postFile) > 0;
                    if (!insertResult) throw new HException("파일 추가 실패");
                }

                // 파일 삽입 후 해당 animalId에 대한 최신 파일 ID 조회
                Long latestFileId = fileMapper.selectLatestFileIdByRefId(animalId, "ANI");

                if (latestFileId != null) {
                    petMapper.updatePetFileId(latestFileId, animalId);
                }
            }

            return result;
        } catch (Exception e) {
            log.error("반려동물 등록 실패 animalId={}, animalName={}", pet.getAnimalId(), pet.getAnimalName(), e);
            throw new HException("반려동물 등록 실패", e);
        }
    }
	
    @Override
    @Transactional
    public boolean updatePet(Pet pet) {
        boolean updated = petMapper.updatePet(pet) > 0;

        if (updated && pet.getFiles() != null && !pet.getFiles().isEmpty()) {
            try {

                List<PostFile> fileList = FileUploadUtil.uploadFiles(
                    pet.getFiles(), "pet", pet.getAnimalId(), "ANI", pet.getUpdateId()
                );

                for (PostFile postFile : fileList) {
                    boolean insertResult = fileMapper.updateFilesByKey(postFile) > 0;
                    if (!insertResult) throw new HException("파일 업데이트 실패");
                }

                // Step 3: 최신 파일 ID로 업데이트
                Long latestFileId = fileMapper.selectLatestFileIdByRefId(pet.getAnimalId(), "ANI");
                if (latestFileId != null) {
                    petMapper.updatePetFileId(latestFileId, pet.getAnimalId());
                }
            } catch (IOException e) {
                throw new HException("파일 업로드 중 오류 발생", e);
            }
        }

        return updated;
    }
    
    @Override
    @Transactional
    public boolean deletePet(int animalId, String usersId) {
        try {
            // ❌ 자식 삭제 제거
            // petTrainingAndActionMapper.logicalDeleteByAnimalId(animalId, usersId);

            // ✅ 부모(동물)만 논리 삭제
            return petMapper.deletePetByIdAndUser(animalId, usersId) > 0;
        } catch (Exception e) {
            log.error("반려동물 삭제 실패", e);
            throw new HException("삭제 실패", e);
        }
    }

    @Override
    public Pet getPetById(int animalId, String usersId, String category) {
        return petMapper.getPetByIdAndUsername(animalId, usersId, category);
    }

	@Override
	public List<Pet> petIdList(Pet pet) {
		return petMapper.petIdList(pet);
	}
}
