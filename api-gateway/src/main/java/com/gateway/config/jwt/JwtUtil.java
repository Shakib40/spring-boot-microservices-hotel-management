// package com.gateway.config.jwt;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jws;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;

// import java.nio.charset.StandardCharsets;
// import java.security.Key;

// public class JwtUtil {
//     private final Key key;

//     public JwtUtil(String secret) {
//         this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//     }

//     public boolean validateToken(String token) {
//         try {
//             parseClaims(token);
//             return true;
//         } catch (Exception ex) {
//             return false;
//         }
//     }

//     public Jws<Claims> parseClaims(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(key)
//                 .build()
//                 .parseClaimsJws(token);
//     }
// }
