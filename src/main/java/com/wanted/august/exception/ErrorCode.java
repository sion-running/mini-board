package com.wanted.august.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password"),
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "System error occurred"),
    ;

    private final HttpStatus status;
    private final String desc;
}
