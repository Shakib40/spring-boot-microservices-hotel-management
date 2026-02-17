package com.auth.entity;

import java.time.LocalDateTime;

// we can store the otp in the database for the user to verify the user as we are already using redis for the otp
public class OTP {
    private String id;
    private String otp;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
}
