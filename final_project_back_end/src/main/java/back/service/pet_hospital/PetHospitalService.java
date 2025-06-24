package back.service.pet_hospital;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import back.model.common.CustomUserDetails;
import back.model.pet_hospital.PetHospital;

public interface PetHospitalService {

	PetHospital registerHospitalRecord(PetHospital petHospital);

	List<PetHospital> getAllByCreateDtDesc();

	void updatePetHospital(int id, PetHospital petHospital, CustomUserDetails userDetails);

	void deletePetHospital(Long id, String usersId);	

}
