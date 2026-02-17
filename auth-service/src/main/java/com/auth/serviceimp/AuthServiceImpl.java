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
import lombok.extern.slf4j.Slf4j;

import com.auth.service.AuthService;
import com.auth.config.Redis.TokenStoreService;
import com.auth.config.jwt.JwtUtil;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenStoreService redisService;
    private final KafkaTemplate<String, User> kafkaTemplate;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration failed: Username {} already exists", request.getUsername());
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email {} already exists", request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        // Fetch Role from User Service via Feign Client
        RoleResponse roleResponse = userServiceClient.getRoleByName(request.getRole());
        if (roleResponse == null) {
            log.error("Registration failed: Role {} not found in User Service", request.getRole());
            throw new RuntimeException("Role not found in User Service: " + request.getRole());
        }

        // Register only when user role is not "ADMIN"
        if (!roleResponse.getName().equals("ADMIN")) {
            log.error("Registration failed: Role {} is not allowed to register", request.getRole());
            throw new RuntimeException("Role not allowed to register: " + request.getRole());
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
        log.info("Saved new user {} to repository", user.getUsername());

        return new RegisterResponse(true, "User registered successfully!");
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("Login failed: User {} not found", request.getUsername());
                    return new RuntimeException("Invalid username or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: Invalid password for user {}", request.getUsername());
            throw new RuntimeException("Invalid username or password");
        }

        // Generate tokens using userId
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        log.debug("Generated access and refresh tokens for user id: {}", user.getId());

        // Store in Redis using TokenStoreService
        redisService.storeAccessToken(accessToken, user.getId());
        redisService.storeRefreshToken(refreshToken, user.getId());
        log.debug("Stored tokens in Redis for user id: {}", user.getId());

        // Send login notification via Kafka
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

    // ✅ Refresh token logic
    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            log.warn("Token refresh failed: Invalid refresh token");
            throw new RuntimeException("Invalid refresh token");
        }

        String userId = jwtUtil.extractUserId(refreshToken);

        // Check refresh token in Redis
        if (!redisService.isRefreshTokenValid(refreshToken)) {
            log.warn("Token refresh failed: Refresh token expired or invalid in Redis for user id: {}", userId);
            throw new RuntimeException("Refresh token expired or invalid");
        }

        // Generate new access token
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        redisService.storeAccessToken(newAccessToken, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Refresh failed: User {} not found", userId);
                    return new RuntimeException("Invalid refresh token");
                });

        return new LoginResponse(user, "Token refreshed!", newAccessToken, refreshToken);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        // Delete both tokens from Redis
        redisService.deleteAccessToken(accessToken);
        redisService.deleteRefreshToken(refreshToken);
        log.info("Deleted tokens from Redis for logout");
    }
}
