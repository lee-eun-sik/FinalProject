package back.util;

public enum ResponseCode {
    SUCCESS(200, "요청이 성공적으로 처리되었습니다."),
    EMPTY_FIELD(400, "{field} 필수 입력값이 누락되었습니다."),
    INVALID_LENGTH(401, "{field} 입력값 길이가 올바르지 않습니다."),
    INVALID_NUMBER(402, "{field} 숫자 형식이 올바르지 않습니다."),
    INVALID_EMAIL(403, "{field} 올바른 이메일 형식을 입력해주세요."),
    WEAK_PASSWORD(404, "{field} 비밀번호는 최소 8자, 영문, 숫자, 특수문자를 포함해야 합니다."),
    INVALID_PHONE(405, "{field} 올바른 전화번호 형식을 입력해주세요."),
    SERVER_ERROR(500, "서버 내부 오류가 발생했습니다.");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageWithField(String field) {
        return message.replace("{field}", field);  // 메시지에서 {field}를 해당 필드로 교체
    }
}
