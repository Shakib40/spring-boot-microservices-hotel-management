package com.auth.config.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenStoreService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void storeAccessToken(String token, String userId) {
        redisTemplate.opsForValue().set("access:" + token, userId, 15, TimeUnit.MINUTES);
    }

    public void storeRefreshToken(String token, String userId) {
        redisTemplate.opsForValue().set("refresh:" + token, userId, 7, TimeUnit.DAYS);
    }

    public boolean isAccessTokenValid(String token) {
        return redisTemplate.hasKey("access:" + token);
    }

    public boolean isRefreshTokenValid(String token) {
        return redisTemplate.hasKey("refresh:" + token);
    }

    public void deleteAccessToken(String token) {
        redisTemplate.delete("access:" + token);
    }

    public void deleteRefreshToken(String token) {
        redisTemplate.delete("refresh:" + token);
    }

    public void storeOtp(String username, String otp) {
        redisTemplate.opsForValue().set("otp:" + username, otp, 5, TimeUnit.MINUTES);
    }

    public String getOtp(String username) {
        return (String) redisTemplate.opsForValue().get("otp:" + username);
    }

    public void deleteOtp(String username) {
        redisTemplate.delete("otp:" + username);
    }

    public void storeAttemptCount(String username, int count) {
        redisTemplate.opsForValue().set("attempt:" + username, count, 5, TimeUnit.MINUTES);
    }

    public int getAttemptCount(String username) {
        Object count = redisTemplate.opsForValue().get("attempt:" + username);
        return count != null ? (int) count : 0;
    }

    public void deleteAttemptCount(String username) {
        redisTemplate.delete("attempt:" + username);
    }

    public void storeBlockedUser(String username) {
        redisTemplate.opsForValue().set("blocked:" + username, "true", 20, TimeUnit.MINUTES);
    }

    public boolean isUserBlocked(String username) {
        return redisTemplate.hasKey("blocked:" + username);
    }

    public void deleteBlockedUser(String username) {
        redisTemplate.delete("blocked:" + username);
    }
}
