package back.mapper.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import back.model.user.User;

@Mapper
public interface UserMapper {
	
	public int registerUser(User user);
	
	public User getUserById(String usersId);
	
	public int updateUser(User user);
	
	public int deleteUser(User user);
	
	public int getTotalUserCount(User user);
	
	public List<User> getUserList(User user);
	
	public int userM(User user);
	
	public int usersIdCheck(User user);

	public int checkUserIdDuplicate(String usersId);

	public List<User> selectUsersByEmail(String email);

	public User findUserByUserIdAndEmail(Map<String, Object> params);

	public int updatePassword(Map<String, Object> params);

	public User findByUserId(String usersId);

	public int isEmailRegistered(String email);
}
