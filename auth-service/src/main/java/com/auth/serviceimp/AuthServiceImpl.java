package com.auth.serviceimp;

import com.auth.dto.LoginResponse;
import com.auth.entity.UserResponse;
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
    private final KafkaTemplate<String, UserResponse> kafkaTemplate;

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
        userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Generate OTP failed: User {} not found", username);
                    return new RuntimeException("User not found");
                });

        String otp = generateRandomOtp();
        redisService.storeOtp(username, otp);
        System.out.println("OTPOTP: " + otp);
        log.info("Generated OTP: {} for user: {}", otp, username); // In production, send via Email/SMS
        return true;
    }

    private String generateRandomOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // 6-digit OTP
    }

    @Override
    public LoginResponse verifyOtp(String username, String otp) {
        UserResponse user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Verify OTP failed: User {} not found", username);
                    return new RuntimeException("User not found");
                });

        String storedOtp = redisService.getOtp(username);
        if (storedOtp == null || !storedOtp.equals(otp)) {
            log.warn("Verify OTP failed: Invalid or expired OTP for user {}", username);
            throw new RuntimeException("Invalid or expired OTP");
        }

        redisService.deleteOtp(username);

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
