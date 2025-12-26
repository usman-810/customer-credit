package com.creditcard.api_gateway.filter;



import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Component
@Slf4j
public class RequestResponseLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Instant startTime = Instant.now();

        log.info("================== Request Start ==================");
        log.info("Request ID: {}", request.getId());
        log.info("Method: {}", request.getMethod());
        log.info("Path: {}", request.getPath());
        log.info("Remote Address: {}", request.getRemoteAddress());
        log.info("Headers: {}", request.getHeaders());
        log.info("==================================================");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            Duration duration = Duration.between(startTime, Instant.now());

            log.info("================== Response End ==================");
            log.info("Request ID: {}", request.getId());
            log.info("Status Code: {}", response.getStatusCode());
            log.info("Response Headers: {}", response.getHeaders());
            log.info("Duration: {} ms", duration.toMillis());
            log.info("==================================================");
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}