package com.creditcard.api_gateway.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.creditcard.api_gateway.util.JwtUtil;

import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    // Public endpoints that don't require authentication
    private static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh",
            "/actuator/health",
            "/swagger-ui",
            "/v3/api-docs"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        log.debug("Processing request: {} {}", request.getMethod(), path);

        // Allow public endpoints
        if (isPublicUrl(path)) {
            log.debug("Public URL, skipping authentication: {}", path);
            return chain.filter(exchange);
        }

        // Extract token from Authorization header
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for: {}", path);
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            // Validate token
            if (!jwtUtil.validateToken(token)) {
                log.warn("Invalid or expired token for: {}", path);
                return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }

            // Extract user information
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            log.info("Authenticated request - User: {}, Role: {}, Path: {}", username, role, path);

            // Add user information to headers for downstream services
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-Auth-User", username)
                    .header("X-Auth-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
            return onError(exchange, "JWT validation failed", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isPublicUrl(String path) {
        return PUBLIC_URLS.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        String errorResponse = String.format(
                "{\"success\":false,\"message\":\"%s\",\"timestamp\":\"%s\"}",
                message,
                java.time.LocalDateTime.now()
        );

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(errorResponse.getBytes()))
        );
    }

    @Override
    public int getOrder() {
        return -100; // High priority
    }
}