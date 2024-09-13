package com.wanted.august.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    CONNECTION("CONNECTION", "Successfully connected."),
    POST_CREATED("POST_CREATION", "New Post created."),
    ;

    private final String type;
    private final String desc;
}
