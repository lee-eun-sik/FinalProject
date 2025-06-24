package back.mapper.pet;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import back.model.alarm.Alarm;
import back.model.pet.Pet;

@Mapper
public interface PetMapper {
	public int insertPet(Pet pet);
	public int updatePetFileId(@Param("fileId") Long fileId, @Param("animalId") int animalId);
	public int updatePet(Pet pet);
	int deletePetByIdAndUser(@Param("animalId") int animalId, @Param("usersId") String usersId);
	
	Pet getPetByIdAndUsername(@Param("animalId") int animalId, @Param("usersId") String usersId,  @Param("category") String category);
	
	public List<Pet> petIdList(Pet pet);
}
