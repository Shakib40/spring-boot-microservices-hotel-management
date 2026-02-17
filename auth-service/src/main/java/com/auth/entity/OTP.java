package com.auth.entity;

import java.time.LocalDateTime;

public class OTP {
    private String id;
    private String otp;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
}
