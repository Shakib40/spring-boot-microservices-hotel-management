package com.auth.dto;

import com.auth.entity.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private UserResponse user;
    private String message;
    private String accessToken;
    private String refreshToken;
}
