package com.luongchivi.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTED(1001, "User already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "User not existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "User not found", HttpStatus.NOT_FOUND),
    UNCATEGORIZED_ERROR(1111, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_MESSAGE_KEY(1004, "Invalid message key", HttpStatus.BAD_REQUEST),
    USERNAME_TOO_SHORT(1005, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT(1006, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN_ACCESS(1008, "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    PERMISSION_ALREADY_EXISTED(1009, "Permission already existed", HttpStatus.BAD_REQUEST),
    ROLE_ALREADY_EXISTED(1010, "Role already existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1011, "Role not found", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(1012, "Permission not found", HttpStatus.NOT_FOUND),
    INVALID_DATE_OF_BIRTH(1013, "Invalid date of birth, your age must be at least {min}", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
