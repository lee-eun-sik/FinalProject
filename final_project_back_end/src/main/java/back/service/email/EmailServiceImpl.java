package back.service.email;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import back.exception.HException;
import back.mapper.auto.AutoInfoMapper;
import back.model.auto.AutoInfo;
import back.model.user.User;
import back.service.user.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private AutoInfoMapper autoInfoMapper;

    private final ConcurrentHashMap<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();

    /**
     * 인증 코드 이메일 전송
     */
    @Override
    public boolean sendVerificationCodeEmail(String to, String code) {
        try {
            String subject = "이메일 인증번호";
            String text = "인증번호는 [" + code + "] 입니다. 3분 내로 입력해주세요.";
            return sendEmail(to, subject, text);
        } catch (Exception e) {
            log.error("인증 이메일 전송 실패", e);
            throw new HException("이메일 전송 중 오류 발생", e);
        }
    }

    /**
     * 일반 이메일 전송
     */
    @Override
    public boolean sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("dldmstlr1999@gmail.com");
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error("이메일 전송 실패", e);
            throw new HException("이메일 전송 실패", e);
        }
    }

    /**
     * 인증 코드 임시 저장
     */
    @Override
    public void saveVerificationCode(String email, String code) {
        verificationCodes.put(email, new VerificationInfo(code, LocalDateTime.now()));
    }

    /**
     * 인증 코드 검증 (메모리)
     */
    @Override
    public boolean verifyCode(String email, String code) {
        try {
            VerificationInfo info = verificationCodes.get(email);
            return info != null &&
                   info.getCode().equals(code) &&
                   LocalDateTime.now().isBefore(info.getGeneratedTime().plusMinutes(3));
        } catch (Exception e) {
            log.error("이메일 코드 검증 실패", e);
            throw new HException("이메일 코드 검증 실패", e);
        }
    }

    /**
     * 이미 등록된 이메일인지 확인
     */
    @Override
    public boolean isEmailRegistered(String email) {
        try {
            return userService.isEmailRegistered(email);
        } catch (Exception e) {
            log.error("이메일 등록 여부 확인 실패", e);
            throw new HException("이메일 중복 체크 실패", e);
        }
    }

    /**
     * 인증번호 저장 (DB + 메모리)
     */
    @Override
    @Transactional
    public boolean saveEmailCode(String email, String code) {
        try {
            // 가입 여부는 무시하고 이메일 인증번호만 저장
            AutoInfo existing = autoInfoMapper.selectByEmail(email);
            if (existing == null) {
                AutoInfo info = new AutoInfo();
                info.setUsersId(null); // 사용자 ID 없음
                info.setAuthEmail(email);
                info.setAuthNumber(code);
                info.setCreateId("system");
                info.setUpdateId("system");
                autoInfoMapper.insertAuthInfo(info);
            } else {
                existing.setAuthNumber(code);
                existing.setUpdateId("system");
                autoInfoMapper.updateAuthNumber(existing);
            }

            saveVerificationCode(email, code);
            return true;
        } catch (Exception e) {
            log.error("이메일 코드 저장 실패", e);
            throw new HException("이메일 코드 저장 실패", e);
        }
    }

    /**
     * 인증번호 최종 검증 및 인증 처리
     */
    @Override
    @Transactional
    public boolean verifyEmailCode(String email, String code) {
        try {
            VerificationInfo info = verificationCodes.get(email);
            if (info == null || !info.getCode().equals(code)) return false;
            if (LocalDateTime.now().isAfter(info.getGeneratedTime().plusMinutes(3))) return false;

            autoInfoMapper.verifyAuthEmail(email); // DB 인증 완료 처리
            return true;
        } catch (Exception e) {
            log.error("이메일 인증 실패", e);
            throw new HException("이메일 인증 처리 실패", e);
        }
    }

    /**
     * 인증정보 보관용 내부 클래스
     */
    private static class VerificationInfo {

        private final String code;
        private final LocalDateTime generatedTime;

        public VerificationInfo(String code, LocalDateTime generatedTime) {
            this.code = code;
            this.generatedTime = generatedTime;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getGeneratedTime() {
            return generatedTime;
        }
    }
   
    @Override
    @Transactional
    public void updateUsersIdToAuthInfo(String email, String usersId) {
    	try {
	        Map<String, Object> paramMap = new HashMap<>();
	        paramMap.put("email", email);
	        paramMap.put("usersId", usersId);
	        autoInfoMapper.updateUsersId(paramMap);
    	}    
        catch (Exception e) {
        	log.error("이메일 인증정보에서 사용자 아이디 업데이트중 오류발생");
        	throw new  HException("이메일 인증정보에서 사용자 아이디 업데이트가 안됬습니다.", e);		
        }
    }

	@Override
	public boolean isEmailYnCheck(String email) {
		try {
            return userService.isEmailRegistered(email);
        } catch (Exception e) {
            log.error("이메일 등록 여부 확인 실패", e);
            throw new HException("이메일 중복 체크 실패", e);
        }
	}
}