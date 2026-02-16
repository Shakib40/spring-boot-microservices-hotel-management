package com.notification.consumer;

import com.notification.dto.ActivityRequest;
import com.notification.dto.NotificationRequest;
import com.notification.service.EmailService;
import com.notification.service.ActivityService;
import com.notification.enums.ActivityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final EmailService emailService;
    private final ActivityService activityService;

    @KafkaListener(topics = "login-alert", groupId = "notification-group")
    public void consumeLoginNotification(NotificationRequest request) {
        try {
            emailService.sendEmailWithTemplate(request);
            activityService
                    .createActivity(new ActivityRequest("Login Alert", "Login Alert", ActivityType.LOGIN, "USER-1"));
        } catch (Exception e) {
            log.error("Failed to send email notification: {}", e.getMessage());
        }
    }
}
