package com.auth.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:900000}") // Default 15 mins
    private long accessTokenExpiry;

    @Value("${jwt.refreshExpiration:604800000}") // Default 7 days
    private long refreshTokenExpiry;

    private Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ✅ Generate Access Token using userId
    public String generateAccessToken(String userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // Store userId as subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Generate Refresh Token using userId
    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // Store userId
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract UserId from Token
    public String extractUserId(String token) {
        return String.valueOf(parseClaims(token).getBody().getSubject());
    }

    // ✅ Validate Token
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    // ✅ Get Expiration Time
    public Date getExpirationDate(String token) {
        return parseClaims(token).getBody().getExpiration();
    }
}
