package com.notification.service;

import com.notification.dto.NotificationRequest;

public interface NotificationService {
    void sendEmail(NotificationRequest request);

    void sendAlert(NotificationRequest request);

    void sendOtp(NotificationRequest request);

    void sendMessage(NotificationRequest request);
}
