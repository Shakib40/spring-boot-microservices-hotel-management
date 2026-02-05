package main.java.com.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String recipient;
    private String subject;
    private String content;
    private String type; // EMAIL, ALERT, OTP, MESSAGE
}
