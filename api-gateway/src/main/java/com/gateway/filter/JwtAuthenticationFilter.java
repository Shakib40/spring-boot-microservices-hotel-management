// package com.gateway.filter;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.cloud.gateway.filter.GatewayFilter;
// import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.http.server.reactive.ServerHttpResponse;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;

// import javax.crypto.SecretKey;
// import java.nio.charset.StandardCharsets;

// @Component
// public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

//     private static final String SECRET_KEY = "mySecretKey12345678901234567890123456789012345678901234567890";

//     public JwtAuthenticationFilter() {
//         super(Config.class);
//     }

//     @Override
//     public GatewayFilter apply(Config config) {
//         return (exchange, chain) -> {
//             ServerHttpRequest request = exchange.getRequest();
            
//             // Skip authentication for auth endpoints
//             if (request.getURI().getPath().contains("/auth/")) {
//                 return chain.filter(exchange);
//             }

//             String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
//             if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                 return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
//             }

//             String token = authHeader.substring(7);
            
//             try {
//                 SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//                 Claims claims = Jwts.parserBuilder()
//                         .setSigningKey(key)
//                         .build()
//                         .parseClaimsJws(token)
//                         .getBody();
                
//                 // Add user info to request headers
//                 ServerHttpRequest modifiedRequest = request.mutate()
//                         .header("X-User-Id", claims.getSubject())
//                         .header("X-User-Role", claims.get("role", String.class))
//                         .build();
                
//                 return chain.filter(exchange.mutate().request(modifiedRequest).build());
                
//             } catch (Exception e) {
//                 return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
//             }
//         };
//     }

//     private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
//         ServerHttpResponse response = exchange.getResponse();
//         response.setStatusCode(httpStatus);
//         return response.setComplete();
//     }

//     public static class Config {
//         // Configuration properties if needed
//     }
// }
