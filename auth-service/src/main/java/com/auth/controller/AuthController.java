package com.auth.controller;

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

    // This will be for If token is expired
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
        log.info("Received token refresh request");
        LoginResponse response = authService.refreshToken(refreshToken);
        log.info("Token refreshed successfully for user id: {}", response.getUser().getId());
        return ResponseEntity.ok(response);
    }

    // ✅ Logout API
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String bearerToken,
            @RequestParam("refreshToken") String refreshToken) {
        log.info("Received logout request");
        String accessToken = bearerToken.replace("Bearer ", "");
        authService.logout(accessToken, refreshToken);
        log.info("User logged out successfully");
        return ResponseEntity.ok("Logged out successfully!");
    }

    // Generate OTP
    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestParam("username") String username) {
        log.info("Received generate OTP request for user: {}", username);
        authService.generateOtp(username);
        log.info("User {} generated OTP successfully", username);
        return ResponseEntity.ok("OTP generated successfully!");
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponse> verifyOtp(@RequestParam("username") String username,
            @RequestParam("otp") String otp) {
        log.info("Received verify OTP request for user: {}", username);
        LoginResponse response = authService.verifyOtp(username, otp);
        log.info("User {} verified OTP successfully", username);
        return ResponseEntity.ok(response);
    }
}
