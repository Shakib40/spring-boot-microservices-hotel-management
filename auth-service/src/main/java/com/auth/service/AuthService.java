package com.auth.service;

import com.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse refreshToken(String refreshToken);

    void logout(String accessToken, String refreshToken);

    boolean generateOtp(String username);

    LoginResponse verifyOtp(String username, String otp);
}
