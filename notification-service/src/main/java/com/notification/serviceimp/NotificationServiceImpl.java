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
    public void sendEmail(NotificationRequest request) {
        System.out.println("CALLING 24" + request);
        log.info("Sending email to: {}", request.getRecipient());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getRecipient());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());
        mailSender.send(message);
        log.info("Email sent successfully to: {}", request.getRecipient());
    }

    @Override
    public void sendAlert(NotificationRequest request) {
        log.info("Sending alert to: {}. Subject: {}. Content: {}",
                request.getRecipient(), request.getSubject(), request.getContent());
        // Implementation for alerts (e.g., push notifications, logging, etc.)
    }

    @Override
    public void sendOtp(NotificationRequest request) {
        log.info("Sending OTP to: {}. Content: {}", request.getRecipient(), request.getContent());
        // Implementation for OTP (e.g., SMS gateway)
    }

    @Override
    public void sendMessage(NotificationRequest request) {
        log.info("Sending message to: {}. Content: {}", request.getRecipient(), request.getContent());
        // Implementation for messages (e.g., SMS, WhatsApp, etc.)
    }
}
