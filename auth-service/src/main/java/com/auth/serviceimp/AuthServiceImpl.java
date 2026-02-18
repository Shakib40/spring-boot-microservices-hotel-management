package com.auth.serviceimp;

import com.auth.dto.LoginResponse;
import com.auth.entity.OTP;
import com.auth.entity.UserResponse;
import com.auth.repository.OTPRepository;
import com.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.auth.service.AuthService;
import com.auth.config.Redis.TokenStoreService;
import com.auth.config.jwt.JwtUtil;

import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TokenStoreService redisService;
    private final OTPRepository otpRepository;
    private final KafkaTemplate<String, UserResponse> kafkaTemplate;
    private final KafkaTemplate<String, String> otpKafkaTemplate;

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            log.warn("Token refresh failed: Invalid refresh token");
            throw new RuntimeException("Invalid refresh token");
        }

        String userId = jwtUtil.extractUserId(refreshToken);

        if (!redisService.isRefreshTokenValid(refreshToken)) {
            log.warn("Token refresh failed: Refresh token expired or invalid in Redis for user id: {}", userId);
            throw new RuntimeException("Refresh token expired or invalid");
        }

        String newAccessToken = jwtUtil.generateAccessToken(userId);
        redisService.storeAccessToken(newAccessToken, userId);

        UserResponse user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Refresh failed: User {} not found", userId);
                    return new RuntimeException("Invalid refresh token");
                });

        return new LoginResponse(user, "Token refreshed!", newAccessToken, refreshToken);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        redisService.deleteAccessToken(accessToken);
        redisService.deleteRefreshToken(refreshToken);
        log.info("Deleted tokens from Redis for logout");
    }

    @Override
    public boolean generateOtp(String username) {
        UserResponse user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Generate OTP failed: User {} not found", username);
                    return new RuntimeException("User not found");
                });

        String otp = generateRandomOtp();
        redisService.storeOtp(username, otp);
        System.out.println("OTPOTP: " + otp);

        OTP otpEntity = new OTP();
        otpEntity.setUserName(username);
        otpEntity.setOtp(otp);
        otpRepository.save(otpEntity);

        log.info("Generated OTP: {} for user: {}", otp, username); // In production, send via Email/SMS

        try {
            log.info("Sending generated otp notification to Kafka on topic 'generated-otp' for user: {}",
                    user.getEmail());
            otpKafkaTemplate.send("generated-otp", otp, user)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send generated otp notification to generated-otp: {}",
                                    ex.getMessage());
                        } else {
                            log.info("Generated otp notification sent successfully to Kafka for user: {}",
                                    user.getEmail());
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending Kafka message: {}", e.getMessage());
        }
        return true;
    }

    private String generateRandomOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    @Override
    public LoginResponse verifyOtp(String username, String otp) {
        UserResponse user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Verify OTP failed: User {} not found", username);
                    return new RuntimeException("User not found");
                });

        String storedOtp = redisService.getOtp(username);

        if (storedOtp == null) {
            log.warn("Verify OTP failed: OTP expired for user {}", username);
            throw new RuntimeException("OTP expired");
        }

        if (redisService.isUserBlocked(username)) {
            throw new RuntimeException("User is blocked for 20 minutes due to too many OTP attempts.");
        }

        if (!storedOtp.equals(otp)) {
            log.warn("Verify OTP failed: Invalid OTP for user {}", username);
            int attemptCount = redisService.getAttemptCount(username);
            if (attemptCount >= 2) {
                redisService.storeBlockedUser(username);
                log.warn("Verify OTP failed: Too many attempts for user {}", username);
                redisService.deleteAttemptCount(username);
                throw new RuntimeException("Too many attempts. User blocked for 20 minutes.");
            }
            redisService.storeAttemptCount(username, attemptCount + 1);
            throw new RuntimeException("Invalid OTP. Attempt " + (attemptCount + 1) + " of 3.");
        }

        redisService.deleteOtp(username);
        redisService.deleteAttemptCount(username);
        redisService.deleteBlockedUser(username);

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        log.debug("Generated access and refresh tokens for user id: {}", user.getId());

        redisService.storeAccessToken(accessToken, user.getId());
        redisService.storeRefreshToken(refreshToken, user.getId());
        log.debug("Stored tokens in Redis for user id: {}", user.getId());

        try {
            log.info("Sending login notification to Kafka on topic 'login-alert' for user: {}", user.getEmail());
            kafkaTemplate.send("login-alert", user)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send login notification to login-alert: {}", ex.getMessage());
                        } else {
                            log.info("Login notification sent successfully to Kafka for user: {}", user.getEmail());
                        }
                    });
        } catch (Exception e) {
            log.error("Error sending Kafka message: {}", e.getMessage());
        }

        return new LoginResponse(user, "Login successful!", accessToken, refreshToken);
    }
}
