package com.auth.serviceimp;

import com.auth.dto.LoginRequest;
import com.auth.dto.RegisterRequest;
import com.auth.dto.RegisterResponse;
import com.auth.dto.LoginResponse;
import com.auth.entity.User;
import com.auth.entity.Role;
import com.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import com.auth.service.AuthService;
import com.auth.config.Redis.TokenStoreService;
import com.auth.config.jwt.JwtUtil;
import com.auth.client.UserServiceClient; // user-service
import com.auth.client.dto.RoleResponse;
import com.auth.dto.NotificationRequest;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenStoreService redisService;
    private final UserServiceClient userServiceClient;
    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        System.out.println("ROLEROLE 00" + request);

        // Map incoming role name to Role entity from user-service via Feign Client
        String roleName = request.getRole();
        RoleResponse roleRes = userServiceClient.getRoleByName(roleName);
        System.out.println("ROLEROLE 11" + roleRes);
        System.out.println("ROLEROLE 22" + roleRes);
        if (roleRes == null) {
            throw new RuntimeException("Role not found in user-service: " + roleName);
        }

        Role role = Role.builder()
                .name(roleRes.getName())
                .description(roleRes.getDescription())
                .build();
        System.out.println("ROLEROLE 33" + role);

        User user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(role)
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

        System.out.println("ROLEROLE 00" + request);

        // Send login notification via Kafka
        NotificationRequest notificationRequest = new NotificationRequest(
                user.getEmail(),
                "Login Alert",
                "Hello " + user.getUsername() + ", you have successfully logged into your account.",
                "EMAIL");
        kafkaTemplate.send("login-topic", notificationRequest);

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
