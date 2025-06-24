package back.model.pet_hospital;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import back.model.Model;
import back.model.common.PostFile;
import back.model.diary.Diary;
import back.model.pet.Pet;
import back.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자 자동 생성
@EqualsAndHashCode(callSuper = true)
public class PetHospital extends Model{
	private int animalHospitalTreatmentId;
	private int animalId;
    private String animalVisitDate;
    private String animalHospitalName;
    private String animalMedication;
    private String animalTreatmentType;
    private String animalTreatmentMemo;
    // 공통 컬럼
    private String createId;
    private String createDt;
    private String updateId;
    private String updateDt;
    private String delYn;
    
    private User user;
    
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUsersId(String usersId) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setUsersId(usersId);
    }

}
