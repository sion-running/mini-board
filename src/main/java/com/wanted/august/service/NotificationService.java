package com.wanted.august.service;

import com.wanted.august.model.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter connect(String userName);
    void send(SseEmitter emitter, Notification notification);
}
