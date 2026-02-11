package com.auth.serviceimp;

import com.auth.dto.LoginRequest;
import com.auth.dto.RegisterRequest;
import com.auth.dto.RegisterResponse;
import com.auth.dto.LoginResponse;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.client.dto.RoleResponse;
import com.auth.client.UserServiceClient; // user-service
import lombok.RequiredArgsConstructor;

import com.auth.service.AuthService;
import com.auth.config.Redis.TokenStoreService;
import com.auth.config.jwt.JwtUtil;
import com.auth.dto.NotificationRequest;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenStoreService redisService;
    // private final UserServiceClient userServiceClient; // Removed usage
    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Fetch Role from User Service via Feign Client
        RoleResponse roleResponse = userServiceClient.getRoleByName(request.getRole());
        if (roleResponse == null) {
            throw new RuntimeException("Role not found in User Service: " + request.getRole());
        }

        User user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(roleResponse.getName())
                .build();

        userRepository.save(user);

        return new RegisterResponse(true, "User registered successfully!");
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate tokens using userId
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // Store in Redis using TokenStoreService
        redisService.storeAccessToken(accessToken, user.getId());
        redisService.storeRefreshToken(refreshToken, user.getId());

        // Send login notification via Kafka
        NotificationRequest notificationRequest = new NotificationRequest(
                user.getEmail(),
                "Login Alert",
                "Hello " + user.getUsername() + ", you have successfully logged into your account.",
                "EMAIL");
        try {
            kafkaTemplate.send("login-topic", notificationRequest)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            System.err.println("login-topic" + ex.getMessage());
                        } else {
                            System.out.println("Login notification sent successfully");
                        }
                    });
        } catch (Exception e) {
            System.err.println("Error sending Kafka message: " + e.getMessage());
        }

        return new LoginResponse(user.getId(), "Login successful!", accessToken, refreshToken);
    }

    // ✅ Refresh token logic
    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String userId = jwtUtil.extractUserId(refreshToken);

        // Check refresh token in Redis
        if (!redisService.isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token expired or invalid");
        }

        // Generate new access token
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        redisService.storeAccessToken(newAccessToken, userId);

        return new LoginResponse(userId, "Token refreshed!", newAccessToken, refreshToken);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        // Delete both tokens from Redis
        redisService.deleteAccessToken(accessToken);
        redisService.deleteRefreshToken(refreshToken);
    }
}
