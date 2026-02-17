package com.auth.dto;

import com.auth.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private User user;
    private String message;
    private String accessToken;
    private String refreshToken;
}
