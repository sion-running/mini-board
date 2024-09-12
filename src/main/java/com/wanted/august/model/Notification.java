package com.wanted.august.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Notification {
    private NotificationType type;
    private String id;
    private String message;
}
