package back.mapper.pet_hospital;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import back.model.pet_hospital.PetHospital;
@Mapper
public interface PetHospitalMapper {
	public int insertPetHospital(PetHospital petHospital);

	public List<PetHospital> selectAllByCreateDtDesc();

	public void updatePetHospital(PetHospital petHospital);
	
	void logicalDeleteById(@Param("animalHospitalTreatmentId") Long id,
            @Param("updateId") String updateId);
}
