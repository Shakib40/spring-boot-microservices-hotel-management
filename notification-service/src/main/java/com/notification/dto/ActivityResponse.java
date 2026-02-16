package com.notification.dto;

import com.notification.enums.ActivityType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private Long id;
    private String title;
    private String message;
    private ActivityType activityType;
    private LocalDateTime createdAt;
    private String createdBy;
}
