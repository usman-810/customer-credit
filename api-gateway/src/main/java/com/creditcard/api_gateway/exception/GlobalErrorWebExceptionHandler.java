package com.creditcard.api_gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-1)
@Slf4j
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalErrorWebExceptionHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Error occurred: ", ex);

        // Set response content type
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Build error response
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("path", exchange.getRequest().getPath().value());
        errorResponse.put("method", exchange.getRequest().getMethod().name());
        errorResponse.put("error", ex.getClass().getSimpleName());
        errorResponse.put("message", getMessage(ex));

        // Determine HTTP status
        HttpStatus status = determineHttpStatus(ex);
        exchange.getResponse().setStatusCode(status);
        errorResponse.put("status", status.value());
        errorResponse.put("statusText", status.getReasonPhrase());

        // Serialize to JSON
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(errorResponse);
        } catch (JsonProcessingException e) {
            log.error("Error serializing error response", e);
            bytes = "{\"success\":false,\"message\":\"Internal Server Error\"}".getBytes();
        }

        // Write response
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private HttpStatus determineHttpStatus(Throwable ex) {
        if (ex instanceof ResponseStatusException) {
            HttpStatusCode statusCode = ((ResponseStatusException) ex).getStatusCode();
            return HttpStatus.resolve(statusCode.value());
        }
        
        if (ex instanceof io.jsonwebtoken.JwtException) {
            return HttpStatus.UNAUTHORIZED;
        }
        
        if (ex instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        }
        
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String getMessage(Throwable ex) {
        if (ex instanceof ResponseStatusException) {
            String reason = ((ResponseStatusException) ex).getReason();
            return reason != null ? reason : ex.getMessage();
        }
        
        String message = ex.getMessage();
        return message != null ? message : "An unexpected error occurred";
    }
}