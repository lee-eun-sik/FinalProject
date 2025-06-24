package back.service.user;

import java.util.List;

import back.model.user.User;

public interface UserService {
    /**
     * 사용자 회원가입
     */
	public boolean registerUser(User user);
    
	public boolean validateUser(User user);
	
	public boolean updateUser(User user);
	
	public boolean deleteUser(User user);
    
    public User getUserById(String usersId);
    
    public List<User> getUserList(User user);
    
    public boolean userM(User user);
    
    public boolean usersIdCheck(User user);

	public boolean isUserIdDuplicate(String usersId);

	public List<User> findUsersByInfo(String email);

	public User findUserByUserIdAndEmail(String usersId, String usersEmail);

	public boolean updatePassword(String usersId, String encodedPassword);

	public boolean resetPassword(User user);

	public boolean isEmailRegistered(String email);

	public User findByEmail(String email);

}