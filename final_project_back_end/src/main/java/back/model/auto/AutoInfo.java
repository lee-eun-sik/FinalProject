package back.model.auto;

import java.util.Date;

import back.model.Model;
import back.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AutoInfo extends Model{
    private Long authId;
    private String authEmail;
    private String authEmailVerified = "N";
    private String authNumber;
    private Date createdDt;
    private Date updatedDt;
    private String createId;
    private String updateId;
    private String delYn = "N";
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