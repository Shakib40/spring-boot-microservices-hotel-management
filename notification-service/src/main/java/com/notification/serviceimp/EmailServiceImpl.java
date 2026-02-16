package com.notification.serviceimp;

import com.notification.consumer.dto.UserResponse;
import com.notification.service.EmailService;
import com.notification.template.HtmlTemplateType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmailWithTemplate(UserResponse user, HtmlTemplateType templateType) {
        log.info("Preparing to send HTML email to: {}", user.getEmail());

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("username", user.getUsername());
            context.setVariable("userId", user.getId());
            context.setVariable("email", user.getEmail());

            String templateName = getTemplateName(templateType);
            String htmlContent = templateEngine.process(templateName, context);

            helper.setTo(user.getEmail());
            helper.setSubject(getSubject(templateType));
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("HTML email sent successfully to: {}", user.getEmail());

        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    private String getTemplateName(HtmlTemplateType type) {
        return switch (type) {
            case LOGIN_ALERT -> "login-alert";
            case RESET_PASSWORD -> "reset-password";
            default -> "welcome";
        };
    }

    private String getSubject(HtmlTemplateType type) {
        return switch (type) {
            case LOGIN_ALERT -> "Security Alert: New Login Detected";
            case RESET_PASSWORD -> "Important: Set Your Password";
            case REGISTRATION_SUCCESS -> "Welcome to Hotel Management System!";
            default -> "Notification Service Alert";
        };
    }
}
