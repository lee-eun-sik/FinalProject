package back.service.pet_training_and_action;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import back.exception.HException;
import back.mapper.pet_hospital.PetHospitalMapper;
import back.mapper.pet_training_and_action.PetTrainingAndActionMapper;
import back.model.common.CustomUserDetails;
import back.model.pet_training_and_action.PetTrainingAndAction;
import back.service.pet_hospital.PetHospitalServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class PetTrainingAndActionServiceImpl implements PetTrainingAndActionService{
	private final PetTrainingAndActionMapper petTrainingAndActionMapper;
	
	@Override
	@Transactional
	public PetTrainingAndAction registerTrainingAndActionRecord(PetTrainingAndAction petTrainingAndAction) {
		// TODO Auto-generated method stub
		 try {
			 petTrainingAndActionMapper.insertPetTrainingAndAction(petTrainingAndAction); // 여기서 ID가 petHospital에 set됨 (MyBatis useGeneratedKeys)
             return petTrainingAndAction;
         } catch (Exception e) {
             log.error("동물 훈련/행동 등록 실패", e);
             throw new HException("동물 훈련/행동 등록 실패", e);
         }
	}

	@Override
	@Transactional
	public List<PetTrainingAndAction> getAllByCreateDtDesc() {
		// TODO Auto-generated method stub
		try {
    		return petTrainingAndActionMapper.selectAllByCreateDtDesc();
    	} catch (Exception e) {
    		log.error("동물 훈련/행동 목록 조회 실패", e);
    		throw new HException("동물 훈련/행동 목록 조회 실패", e);
    	}
	}

	@Override
	@Transactional
	public void updatePetTrainingAndAction (int id, PetTrainingAndAction petTrainingAndAction, CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		try {
			petTrainingAndAction.setAnimalTrainingAction(id);
			petTrainingAndAction.setUpdateId(userDetails.getUsername());
			petTrainingAndActionMapper.updatePetTrainingAndAction(petTrainingAndAction);

            // TODO: 파일 저장 로직은 여기에 추가
        } catch (Exception e) {
            log.error("동물 훈련/행동 수정 실패", e);
            throw new HException("동물 훈현/행동 수정 실패", e);
        }
	}

	@Override
    @Transactional
    public void deletePetTrainingAndAction(Long id, String usersId) {
        try {
        	petTrainingAndActionMapper.logicalDeleteById(id, usersId);
        } catch (Exception e) {
            log.error("동물 훈련/행동 삭제 실패", e);
            throw new HException("동물 훈련/행동 삭제 실패", e);
        }
    }

}
