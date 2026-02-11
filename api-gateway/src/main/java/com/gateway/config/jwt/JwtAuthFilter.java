package com.gateway.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private static final String[] PUBLIC_PREFIXES = {
            "/actuator", "/auth/", "/swagger-ui", "/v3/api-docs", "/running", "/api/auth/"
    };

    public JwtAuthFilter(@Value("${jwt.secret}") String secret) {
        System.out.println("🔑 Loaded JWT Secret: " + secret);
        this.jwtUtil = new JwtUtil(secret);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) {
                return chain.filter(exchange);
            }
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            if (!jwtUtil.validateToken(token)) {
                System.out.println("❌ Invalid JWT Token");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        } catch (Exception ex) {
            System.out.println("❌ JWT Exception: " + ex.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Highest priority (executes before route filters)
    }
}
