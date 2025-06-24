package back.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    // 입력값이 비어 있는지 확인
    public static ResponseCode validateEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? ResponseCode.EMPTY_FIELD : ResponseCode.SUCCESS;
    }

    // 최소 길이 체크
    public static ResponseCode validateMinLength(String value, int length) {
        return (value != null && value.length() >= length) ? ResponseCode.SUCCESS : ResponseCode.INVALID_LENGTH;
    }

    // 최대 길이 체크
    public static ResponseCode validateMaxLength(String value, int length) {
        return (value != null && value.length() <= length) ? ResponseCode.SUCCESS : ResponseCode.INVALID_LENGTH;
    }

    // 숫자 형식 체크
    public static ResponseCode validateNumeric(String value) {
        return (value != null && value.matches("^[0-9]+$")) ? ResponseCode.SUCCESS : ResponseCode.INVALID_NUMBER;
    }

    // 이메일 형식 체크
    public static ResponseCode validateEmail(String value) {
        String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return (value != null && Pattern.matches(emailPattern, value)) ? ResponseCode.SUCCESS : ResponseCode.INVALID_EMAIL;
    }

    // 비밀번호 강도 체크 (영문 + 숫자 + 특수문자 포함, 최소 8자 이상)
    public static ResponseCode validatePassword(String value) {
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";
        return (value != null && Pattern.matches(passwordPattern, value)) ? ResponseCode.SUCCESS : ResponseCode.WEAK_PASSWORD;
    }

    // 전화번호 형식 체크 (000-0000-0000 또는 000-000-0000)
    public static ResponseCode validatePhoneNumber(String value) {
        String phonePattern = "^(\\d{2,3})-(\\d{3,4})-(\\d{4})$";
        return (value != null && Pattern.matches(phonePattern, value)) ? ResponseCode.SUCCESS : ResponseCode.INVALID_PHONE;
    }
}
