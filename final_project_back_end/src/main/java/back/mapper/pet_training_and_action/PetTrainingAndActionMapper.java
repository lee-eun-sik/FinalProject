package back.mapper.pet_training_and_action;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import back.model.pet_training_and_action.PetTrainingAndAction;

@Mapper
public interface PetTrainingAndActionMapper {

	public void insertPetTrainingAndAction(PetTrainingAndAction petTrainingAndAction);

	public List<PetTrainingAndAction> selectAllByCreateDtDesc();

	public void updatePetTrainingAndAction(PetTrainingAndAction petTrainingAndAction);

    public void logicalDeleteById(@Param("animalTrainingAction") Long animalTrainingAction, @Param("updateId") String updateId);

}
