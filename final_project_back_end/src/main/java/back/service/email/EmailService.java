package back.service.email;

public interface EmailService {
	public boolean sendVerificationCodeEmail(String to, String code);
	public boolean saveEmailCode(String email, String code);
	public boolean verifyEmailCode(String email, String code);
	public boolean isEmailRegistered(String email);
	public boolean isEmailYnCheck(String email);
	
	public void saveVerificationCode(String email, String code); // 수정됨
	public boolean verifyCode(String email, String code);
	public boolean sendEmail(String to, String subject, String text); // 추가됨
	public void updateUsersIdToAuthInfo(String email, String usersId);
	
}
