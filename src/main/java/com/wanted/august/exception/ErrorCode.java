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
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT, "Duplicate user name"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not found"),
    NO_PERMISSION_FOR_THE_POST(HttpStatus.FORBIDDEN, "User does not have permission for this post"),
    POST_UPDATE_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST, "Update period expired"),
    COMMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "User already has a comment on this post"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not found"),
    NO_PERMISSION_FOR_THE_COMMENT(HttpStatus.FORBIDDEN, "User does not have permission for this comment"),
    NOTIFICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send notification to the user"),

    ;

    private final HttpStatus status;
    private final String desc;
}
