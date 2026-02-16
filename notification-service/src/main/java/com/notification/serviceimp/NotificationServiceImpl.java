package com.notification.serviceimp;

import com.notification.dto.NotificationRequest;
import com.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailWithoutTemplate(NotificationRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("shakibjilani@gmail.com");
        message.setSubject("Login Notification");
        message.setText("You have been logged in successfully");
        mailSender.send(message);
    }

    @Override
    public void sendEmailWithTemplate(NotificationRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getRecipient());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());
        mailSender.send(message);
    }
}
