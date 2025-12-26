package com.creditcard.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Health check aggregation
                .route("health-check", r -> r
                        .path("/health/**")
                        .filters(f -> f
                                .setPath("/actuator/health")
                                .addResponseHeader("X-Health-Check", "true"))
                        .uri("http://localhost:8080"))

                // API documentation route
                .route("api-docs", r -> r
                        .path("/v3/api-docs/**")
                        .uri("http://localhost:8081"))

                .build();
    }
}