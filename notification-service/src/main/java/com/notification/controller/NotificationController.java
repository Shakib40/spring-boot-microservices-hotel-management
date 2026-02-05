package com.notification.controller;

import com.notification.dto.NotificationRequest;
import com.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody NotificationRequest request) {
        notificationService.sendEmail(request);
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/alert")
    public ResponseEntity<String> sendAlert(@RequestBody NotificationRequest request) {
        notificationService.sendAlert(request);
        return ResponseEntity.ok("Alert sent successfully");
    }

    @PostMapping("/otp")
    public ResponseEntity<String> sendOtp(@RequestBody NotificationRequest request) {
        notificationService.sendOtp(request);
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody NotificationRequest request) {
        notificationService.sendMessage(request);
        return ResponseEntity.ok("Message sent successfully");
    }
}
