package com.notification.serviceimp;

import com.notification.dto.NotificationRequest;
import com.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmailWithoutTemplate(NotificationRequest request) {
        log.info("Sending simple email without template to: shakibjilani@gmail.com");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("shakibjilani@gmail.com");
        message.setSubject("Login Notification");
        message.setText("You have been logged in successfully");
        mailSender.send(message);
        log.info("Simple email sent successfully");
    }

    @Override
    public void sendEmailWithTemplate(NotificationRequest request) {
        log.info("Sending email with details to: {}", request.getRecipient());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getRecipient());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());
        mailSender.send(message);
        log.info("Email sent successfully to: {}", request.getRecipient());
    }
}
