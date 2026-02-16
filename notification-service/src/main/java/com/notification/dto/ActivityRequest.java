package com.notification.dto;

import com.notification.enums.ActivityType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {
    private String title;
    private String message;
    private ActivityType activityType;
    private String createdBy;
}
