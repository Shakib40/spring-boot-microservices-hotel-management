package com.gateway.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private static final String[] PUBLIC_PREFIXES = {
            "/actuator", "/auth/", "/swagger-ui", "/v3/api-docs", "/running", "/api/auth/", "/gateway/actuator"
    };

    public JwtAuthFilter(@Value("${jwt.secret}") String secret) {
        log.info("JwtAuthFilter initialized");
        this.jwtUtil = new JwtUtil(secret);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.debug("Processing request for path: {}", path);

        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) {
                log.debug("Path {} is public, bypassing authentication", path);
                return chain.filter(exchange);
            }
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authentication failed: Missing or invalid Authorization header for path: {}", path);
            return onError(exchange, "UNAUTHORIZED", "Failed to get valid token", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        try {
            if (!jwtUtil.validateToken(token)) {
                log.warn("Authentication failed: Invalid JWT Token for path: {}", path);
                return onError(exchange, "UNAUTHORIZED", "Failed to get valid token", HttpStatus.UNAUTHORIZED);
            }
            log.debug("Authentication successful for path: {}", path);
        } catch (Exception ex) {
            log.error("Authentication failed: JWT Exception for path {}: {}", path, ex.getMessage());
            return onError(exchange, "UNAUTHORIZED", "Failed to get valid token", HttpStatus.UNAUTHORIZED);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Highest priority (executes before route filters)
    }

    private Mono<Void> onError(ServerWebExchange exchange, String status, String message, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = String.format("{\"status\": \"%s\", \"message\": \"%s\"}", status, message);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
