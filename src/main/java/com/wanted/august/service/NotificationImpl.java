package com.wanted.august.service;


import com.wanted.august.model.Notification;
import com.wanted.august.model.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationImpl implements NotificationService {
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private static final long TIMEOUT = 60 * 1000;

    @Override
    public SseEmitter connect(String userName) {
        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);

        String eventId = userName + "_" + System.currentTimeMillis();
        emitterMap.put(eventId, sseEmitter);

        sseEmitter.onCompletion(() -> emitterMap.remove(eventId));
        sseEmitter.onTimeout(() -> emitterMap.remove(eventId));
        sseEmitter.onError((e) -> emitterMap.remove(eventId));

        // 초기 연결시 데이터 전송해야 함
        Notification notification = Notification.builder().type(NotificationType.CONNECTION).id(eventId).message("successfully connected").build();
        send(sseEmitter, notification);

        // TODO 초기 연결이 아니라면, 미전송된 데이터 확인 후 전송해야 함

        return sseEmitter;
    }

    @Override
    public void send(SseEmitter emitter, Notification notification) {
        try {
            emitter.send(SseEmitter.event()
                    .name(notification.getType().toString())
                    .id(notification.getId())
                    .data(notification.getMessage())
            );
        } catch (IOException exception) {
            throw new RuntimeException(); // TODO
        }
    }
}
