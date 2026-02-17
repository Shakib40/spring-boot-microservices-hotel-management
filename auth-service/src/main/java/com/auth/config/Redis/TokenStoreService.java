package com.auth.config.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenStoreService {

    private final RedisTemplate<String, Object> redisTemplate;

    // ✅ Store Access Token with Expiry
    public <Sting> void storeAccessToken(String token, Sting userId) {
        redisTemplate.opsForValue().set("access:" + token, userId, 15, TimeUnit.MINUTES);
    }

    // ✅ Store Refresh Token with Expiry
    public <Sting> void storeRefreshToken(String token, Sting userId) {
        redisTemplate.opsForValue().set("refresh:" + token, userId, 7, TimeUnit.DAYS);
    }

    // ✅ Validate Access Token
    public boolean isAccessTokenValid(String token) {
        return redisTemplate.hasKey("access:" + token);
    }

    // ✅ Validate Refresh Token
    public boolean isRefreshTokenValid(String token) {
        return redisTemplate.hasKey("refresh:" + token);
    }

    // ✅ Delete Token on Logout
    public void deleteAccessToken(String token) {
        redisTemplate.delete("access:" + token);
    }

    public void deleteRefreshToken(String token) {
        redisTemplate.delete("refresh:" + token);
    }

    // ✅ Store OTP with short expiry (e.g., 5 minutes)
    public void storeOtp(String username, String otp) {
        redisTemplate.opsForValue().set("otp:" + username, otp, 5, TimeUnit.MINUTES);
    }

    // ✅ Retrieve OTP
    public String getOtp(String username) {
        return (String) redisTemplate.opsForValue().get("otp:" + username);
    }

    // ✅ Delete OTP
    public void deleteOtp(String username) {
        redisTemplate.delete("otp:" + username);
    }
}
