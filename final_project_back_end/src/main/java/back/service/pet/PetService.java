package back.service.pet;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import back.model.alarm.Alarm;
import back.model.pet.Pet;

public interface PetService {
    public boolean registerPet(Pet pet);

	public boolean updatePet(Pet pet);

	public boolean deletePet(int animalId, String usersId);

	public Pet getPetById(int animalId, String usersId, String category);
	
	public List<Pet> petIdList(Pet pet);
	
}