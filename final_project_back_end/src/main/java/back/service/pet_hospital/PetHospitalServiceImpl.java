package back.service.pet_hospital;

import back.exception.HException;
import back.mapper.pet_hospital.PetHospitalMapper;
import back.model.common.CustomUserDetails;
import back.model.pet_hospital.PetHospital;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetHospitalServiceImpl implements PetHospitalService {

    private final PetHospitalMapper petHospitalMapper;

    @Override
    @Transactional
    public PetHospital registerHospitalRecord(PetHospital petHospital) {
        try {
            petHospitalMapper.insertPetHospital(petHospital); // 여기서 ID가 petHospital에 set됨 (MyBatis useGeneratedKeys)
            return petHospital;
        } catch (Exception e) {
            log.error("병원 진료 등록 실패", e);
            throw new HException("병원 진료 등록 실패", e);
        }
    }

    @Override
    public List<PetHospital> getAllByCreateDtDesc() {
    	try {
    		return petHospitalMapper.selectAllByCreateDtDesc();
    	} catch (Exception e) {
    		log.error("병원 진료 목록 조회 실패", e);
    		throw new HException("병원 진료 목록 조회 실패", e);
    	}
    }

    @Override
    @Transactional
    public void updatePetHospital(int id, PetHospital petHospital, CustomUserDetails userDetails) {
        try {
            petHospital.setAnimalHospitalTreatmentId(id);
            petHospital.setUpdateId(userDetails.getUsername());
            petHospitalMapper.updatePetHospital(petHospital);

            // TODO: 파일 저장 로직은 여기에 추가
        } catch (Exception e) {
            log.error("병원 진료 수정 실패", e);
            throw new HException("병원 진료 수정 실패", e);
        }
    }

    @Override
    @Transactional
    public void deletePetHospital(Long id, String usersId) {
        try {
            petHospitalMapper.logicalDeleteById(id, usersId);
        } catch (Exception e) {
            log.error("병원 진료 삭제 실패", e);
            throw new HException("병원 진료 삭제 실패", e);
        }
    }

	
    
    
    
}
