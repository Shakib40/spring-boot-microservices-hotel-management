package com.notification.consumer;

import com.notification.template.HtmlTemplateType;
import com.notification.consumer.dto.UserResponse;
import com.notification.dto.ActivityRequest;
import com.notification.service.EmailService;
import com.notification.service.ActivityService;
import com.notification.enums.ActivityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final EmailService emailService;
    private final ActivityService activityService;

    // @KafkaListener(topics = "login-alert", groupId = "notification-group")
    // public void consumeLoginNotification(UserResponse user) {
    // try {
    // emailService.sendEmailWithTemplate(user, HtmlTemplateType.LOGIN_ALERT);
    // activityService.createActivity(
    // new ActivityRequest("Login Alert", "Login Alert for " + user.getUsername(),
    // ActivityType.LOGIN,
    // user.getId()));
    // } catch (Exception e) {
    // log.error("Failed to send email notification: {}", e.getMessage());
    // }
    // }

    // @KafkaListener(topics = "reset-password", groupId = "notification-group")
    // public void consumeResetPasswordNotification(UserResponse user) {
    // System.out.println("Reset Password Notification: " + user);
    // try {
    // emailService.sendEmailWithTemplate(user, HtmlTemplateType.RESET_PASSWORD);
    // activityService.createActivity(
    // new ActivityRequest("Reset Password", "Reset Password for " +
    // user.getUsername(),
    // ActivityType.PASSWORD_RESET,
    // user.getId()));
    // } catch (Exception e) {
    // log.error("Failed to send email notification: {}", e.getMessage());
    // }
    // }

    // @KafkaListener(topics = "welcome", groupId = "notification-group")
    // public void consumeWelcomeNotification(UserResponse user) {
    // try {
    // emailService.sendEmailWithTemplate(user, HtmlTemplateType.WELCOME_EMAIL);
    // activityService.createActivity(
    // new ActivityRequest("Welcome", "Welcome to our platform " +
    // user.getUsername(),
    // ActivityType.WELCOME,
    // user.getId()));
    // } catch (Exception e) {
    // log.error("Failed to send email notification: {}", e.getMessage());
    // }
    // }
}
