package com.luongchivi.identity_service.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "User existed"),
    USER_NOT_EXISTED(1002, "User not existed"),
    UNCATEGORIZED_EXCEPTION(1111, "Uncategorized error"),
    KEY_ENUM_INVALID(1003, "Invalid message key enum validation"),
    USERNAME_INVALID(1004, "Username must be at least 10 characters"),
    PASSWORD_INVALID(1005, "Password must be at least 8 characters"),
    UNAUTHENTICATED(1005, "Unauthenticated"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
