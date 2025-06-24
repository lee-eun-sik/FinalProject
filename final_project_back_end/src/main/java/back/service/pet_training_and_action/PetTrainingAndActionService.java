package back.service.pet_training_and_action;

import java.util.List;

import back.model.common.CustomUserDetails;
import back.model.pet_training_and_action.PetTrainingAndAction;

public interface PetTrainingAndActionService {

	PetTrainingAndAction registerTrainingAndActionRecord(PetTrainingAndAction petTrainingAndAction);

	List<PetTrainingAndAction> getAllByCreateDtDesc();

	void updatePetTrainingAndAction(int id, PetTrainingAndAction petTrainingAndAction, CustomUserDetails userDetails);

	void deletePetTrainingAndAction(Long trainingAndActionId, String username);

}
