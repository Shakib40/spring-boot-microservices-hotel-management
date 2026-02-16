package com.notification.controller;

import com.notification.dto.NotificationRequest;
import com.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send-email-with-template")
    public ResponseEntity<String> sendEmailWithTemplate(@RequestBody NotificationRequest request) {
        notificationService.sendEmailWithTemplate(request);
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/send-email-without-template")
    public ResponseEntity<String> sendEmailWithoutTemplate(@RequestBody NotificationRequest request) {
        notificationService.sendEmailWithoutTemplate(request);
        return ResponseEntity.ok("Email sent successfully");
    }

}
