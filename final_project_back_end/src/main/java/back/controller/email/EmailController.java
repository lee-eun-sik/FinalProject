package back.controller.email;

import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import back.service.email.EmailService;
import back.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-code.do")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> request) {
        String email = request.get("usersEmail");

        if (email == null || email.isEmpty()) {
            log.info("이메일 누락");
            // 직접 ApiResponse 객체 생성
            ApiResponse<Object> response = new ApiResponse<>(false, "이메일이 누락되었습니다.", null);
            return ResponseEntity.badRequest().body(response);
        }

        // 이미 가입된 이메일인지 확인
        if (emailService.isEmailRegistered(email)) {
            // 중복 이메일 응답 - success=false, message만 전달
            ApiResponse<Object> response = new ApiResponse<>(false, "해당 이메일은 이미 가입되어 있습니다.", null);
            log.info("이메일 이미있음이메일 이미있음이메일 이미있음이메일 이미있음이메일 이미있음");
            return ResponseEntity.ok().body(response);
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        // DB나 메모리/캐시에 저장
        emailService.saveEmailCode(email, code);

        // 이메일 전송
        boolean result = emailService.sendVerificationCodeEmail(email, code);

        if (result) {
            ApiResponse<Object> response = new ApiResponse<>(true, "인증번호가 전송되었습니다.", null);
            return ResponseEntity.ok().body(response);
        } else {
            ApiResponse<Object> response = new ApiResponse<>(false, "이메일 전송 실패", null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/idfind-send-code.do")
    public ResponseEntity<?> idFindSendCode(@RequestBody Map<String, String> request) {
        String email = request.get("usersEmail");

        if (email == null || email.isEmpty()) {
            log.info("이메일 누락");
            // 직접 ApiResponse 객체 생성
            ApiResponse<Object> response = new ApiResponse<>(false, "이메일이 누락되었습니다.", null);
            return ResponseEntity.badRequest().body(response);
        }

        // 이미 가입된 이메일인지 확인   가입된이메일 있을때  여기를 패스해야함.
        if (!emailService.isEmailYnCheck(email)) {
            // 중복 이메일 응답 - success=false, message만 전달
            ApiResponse<Object> response = new ApiResponse<>(false, "해당 이메일은 가입되어있지 않은 이메일입니다.", null);
            log.info("가입안되어있음가입안되어있음가입안되어있음가입안되어있음가입안되어있음");
            return ResponseEntity.ok().body(response);
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        // DB나 메모리/캐시에 저장
        emailService.saveEmailCode(email, code);

        // 이메일 전송
        boolean result = emailService.sendVerificationCodeEmail(email, code);

        if (result) {
            ApiResponse<Object> response = new ApiResponse<>(true, "인증번호가 전송되었습니다.", null);
            return ResponseEntity.ok().body(response);
        } else {
            ApiResponse<Object> response = new ApiResponse<>(false, "이메일 전송 실패", null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/pwfind-send-code.do")
    public ResponseEntity<?> pwFindSendCode(@RequestBody Map<String, String> request) {
        String email = request.get("usersEmail");

        if (email == null || email.isEmpty()) {
            log.info("이메일 누락");
            // 직접 ApiResponse 객체 생성
            ApiResponse<Object> response = new ApiResponse<>(false, "이메일이 누락되었습니다.", null);
            return ResponseEntity.badRequest().body(response);
        }

        // 이미 가입된 이메일인지 확인   가입된이메일 있을때  여기를 패스해야함.
        if (!emailService.isEmailYnCheck(email)) {
            // 중복 이메일 응답 - success=false, message만 전달
            ApiResponse<Object> response = new ApiResponse<>(false, "해당 이메일은 가입되어있지 않은 이메일입니다.", null);
            log.info("가입안되어있음가입안되어있음가입안되어있음가입안되어있음가입안되어있음");
            return ResponseEntity.ok().body(response);
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        // DB나 메모리/캐시에 저장
        emailService.saveEmailCode(email, code);

        // 이메일 전송
        boolean result = emailService.sendVerificationCodeEmail(email, code);

        if (result) {
            ApiResponse<Object> response = new ApiResponse<>(true, "인증번호가 전송되었습니다.", null);
            return ResponseEntity.ok().body(response);
        } else {
            ApiResponse<Object> response = new ApiResponse<>(false, "이메일 전송 실패", null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/verify-code.do")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("usersEmail"); // 대소문자 일치 주의
        String code = request.get("code");

        if (email == null || code == null) {
            log.warn("verify-code 요청에서 email 또는 code가 null입니다. email={}, code={}", email, code);
            ApiResponse<Object> response = new ApiResponse<>(false, "이메일 또는 인증번호가 누락되었습니다.", null);
            return ResponseEntity.badRequest().body(response);
        }

        if (emailService.verifyEmailCode(email, code)) {
            ApiResponse<Object> response = new ApiResponse<>(true, "이메일 인증이 완료되었습니다.", null);
            return ResponseEntity.ok().body(response);
        } else {
            ApiResponse<Object> response = new ApiResponse<>(false, "인증번호 불일치", null);
            return ResponseEntity.badRequest().body(response);
        }
    }
}