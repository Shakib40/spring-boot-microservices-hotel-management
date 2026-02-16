package com.notification.serviceimp;

import com.notification.dto.ActivityRequest;
import com.notification.dto.ActivityResponse;
import com.notification.entity.Activity;
import com.notification.repository.ActivityRepository;
import com.notification.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    @Override
    public ActivityResponse createActivity(ActivityRequest request) {
        log.info("Creating new activity: {}", request.getTitle());
        Activity activity = Activity.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .activityType(request.getActivityType())
                .createdBy(request.getCreatedBy())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        log.info("Activity created with id: {}", savedActivity.getId());
        return mapToResponse(savedActivity);
    }

    @Override
    public List<ActivityResponse> getAllActivities() {
        log.info("Fetching all activities");
        return activityRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityResponse getActivityById(Long id) {
        log.info("Fetching activity with id: {}", id);
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Activity not found with id: {}", id);
                    return new RuntimeException("Activity not found with id: " + id);
                });
        return mapToResponse(activity);
    }

    @Override
    public ActivityResponse updateActivity(Long id, ActivityRequest request) {
        log.info("Updating activity with id: {}", id);
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Update failed: Activity not found with id: {}", id);
                    return new RuntimeException("Activity not found with id: " + id);
                });

        activity.setTitle(request.getTitle());
        activity.setMessage(request.getMessage());
        activity.setActivityType(request.getActivityType());
        activity.setCreatedBy(request.getCreatedBy());

        Activity updatedActivity = activityRepository.save(activity);
        log.info("Activity updated with id: {}", updatedActivity.getId());
        return mapToResponse(updatedActivity);
    }

    @Override
    public void deleteActivity(Long id) {
        log.info("Deleting activity with id: {}", id);
        if (!activityRepository.existsById(id)) {
            log.error("Delete failed: Activity not found with id: {}", id);
            throw new RuntimeException("Activity not found with id: " + id);
        }
        activityRepository.deleteById(id);
        log.info("Activity deleted with id: {}", id);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .message(activity.getMessage())
                .activityType(activity.getActivityType())
                .createdAt(activity.getCreatedAt())
                .createdBy(activity.getCreatedBy())
                .build();
    }
}
