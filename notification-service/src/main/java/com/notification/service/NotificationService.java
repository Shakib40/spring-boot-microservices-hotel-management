package com.notification.service;

import com.notification.dto.NotificationRequest;

public interface NotificationService {
    void sendEmail(NotificationRequest request);

    void sendAlert(NotificationRequest request);

    void sendOtp(NotificationRequest request);

    void sendMessage(NotificationRequest request);

    // sendEmailWithout template
    void sendEmailWithoutTemplate(NotificationRequest request);

    // sendEmailWith template
    void sendEmailWithTemplate(NotificationRequest request);
}
