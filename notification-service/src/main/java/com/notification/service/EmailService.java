package com.notification.service;

import com.notification.consumer.dto.UserResponse;
import com.notification.template.HtmlTemplateType;

public interface EmailService {

    void sendEmailWithTemplate(UserResponse user, HtmlTemplateType templateType);

    void sendOTP(UserResponse user, String otp);

    void sendLoginAlert(UserResponse user);
}
