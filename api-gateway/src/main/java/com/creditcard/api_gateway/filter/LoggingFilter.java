package com.creditcard.api_gateway.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        
        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().toString();
        String remoteAddress = exchange.getRequest().getRemoteAddress().toString();
        
        log.info("Incoming request: {} {} from {}", method, path, remoteAddress);
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            int statusCode = exchange.getResponse().getStatusCode().value();
            
            log.info("Outgoing response: {} {} - Status: {} - Duration: {}ms", 
                method, path, statusCode, duration);
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}