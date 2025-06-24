package back.model.pet_training_and_action;

import back.model.user.User;
import lombok.Data;
@Data
public class PetTrainingAndAction {
	private int animalTrainingAction;
	private int animalId;
	private String animalRecordDate;
	private String animalTrainingType;
	private String animalTrainingMemo;
	
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
