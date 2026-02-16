package com.notification.service;

import com.notification.dto.NotificationRequest;

public interface NotificationService {

    // sendEmailWithout template
    void sendEmailWithoutTemplate(NotificationRequest request);

    // sendEmailWith template
    void sendEmailWithTemplate(NotificationRequest request);
}
