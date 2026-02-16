package com.auth.controller;

import com.auth.dto.LoginRequest;
import com.auth.dto.RegisterRequest;
import com.auth.dto.RegisterResponse;
import com.auth.dto.LoginResponse;
import com.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    // This will be for Register
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request for user: {}", request.getUsername());
        RegisterResponse response = authService.register(request);
        log.info("User {} registered successfully", request.getUsername());
        return ResponseEntity.ok(response);
    }

    // This will be for Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("Received login request for user: {}", request.getUsername());
        LoginResponse response = authService.login(request);
        log.info("User {} logged in successfully", request.getUsername());
        return ResponseEntity.ok(response);
    }

    // This will be for If token is expired
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
        log.info("Received token refresh request");
        LoginResponse response = authService.refreshToken(refreshToken);
        log.info("Token refreshed successfully for user id: {}", response.getUserId());
        return ResponseEntity.ok(response);
    }

    // ✅ Logout API
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String bearerToken,
            @RequestParam String refreshToken) {
        log.info("Received logout request");
        String accessToken = bearerToken.replace("Bearer ", "");
        authService.logout(accessToken, refreshToken);
        log.info("User logged out successfully");
        return ResponseEntity.ok("Logged out successfully!");
    }
}
