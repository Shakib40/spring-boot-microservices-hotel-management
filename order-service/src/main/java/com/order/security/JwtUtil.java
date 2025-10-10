// package com.order.security;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;

// import java.nio.charset.StandardCharsets;
// import java.security.Key;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// @Component
// public class JwtUtil {
//     private final Key key;

//     public JwtUtil(@Value("${jwt.secret}") String secret) {
//         this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//     }

//     public Claims parseClaims(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(key)
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }
// }
