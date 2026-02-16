package com.notification.service;

import com.notification.dto.ActivityRequest;
import com.notification.dto.ActivityResponse;

import java.util.List;

public interface ActivityService {
    ActivityResponse createActivity(ActivityRequest request);

    List<ActivityResponse> getAllActivities();

    ActivityResponse getActivityById(Long id);

    ActivityResponse updateActivity(Long id, ActivityRequest request);

    void deleteActivity(Long id);
}
