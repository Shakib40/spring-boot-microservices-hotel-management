package com.notification.service;

import com.notification.dto.NotificationRequest;

public interface EmailService {

    // sendEmailWithout template
    void sendEmailWithoutTemplate(NotificationRequest request);

    // sendEmailWith template
    void sendEmailWithTemplate(NotificationRequest request);
}
