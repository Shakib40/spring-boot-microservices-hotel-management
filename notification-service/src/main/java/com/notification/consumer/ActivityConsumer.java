package com.notification.consumer;

import com.notification.dto.ActivityRequest;
import com.notification.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityConsumer {

    private final ActivityService activityService;

    @KafkaListener(topics = "activity-logs", groupId = "notification-group")
    public void consumeActivityNotification(ActivityRequest activity) {
        try {
            activityService.createActivity(
                    new ActivityRequest(
                            activity.getTitle(),
                            activity.getMessage(),
                            activity.getActivityType(),
                            activity.getCreatedBy()));
        } catch (Exception e) {
            log.error("Failed to logs activity", e.getMessage());
        }
    }

}
