package com.wanted.august.service;

import com.wanted.august.model.Notification;
import com.wanted.august.model.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter connect(String userName);
    SseEmitter send(NotificationType type, String userName);
}
