package com.notification.consumer;

import com.notification.dto.NotificationRequest;
import com.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "login-topic", groupId = "notification-group")
    public void consumeLoginNotification(NotificationRequest request) {
        System.out.println("CALLING NOTIFICATION_ERVICE" + request);
        try {
            notificationService.sendEmail(request);
        } catch (Exception e) {
            log.error("Failed to send email notification: {}", e.getMessage());
        }
    }
}
