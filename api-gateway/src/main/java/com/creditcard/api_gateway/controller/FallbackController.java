package com.creditcard.api_gateway.controller;



import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
@Slf4j
public class FallbackController {

    @GetMapping("/customers")
    @PostMapping("/customers")
    public ResponseEntity<Map<String, Object>> customerFallback() {
        log.error("Customer Service is unavailable. Fallback triggered.");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Customer Service is temporarily unavailable. Please try again later.");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Customer Service");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/cards")
    @PostMapping("/cards")
    public ResponseEntity<Map<String, Object>> cardFallback() {
        log.error("Card Service is unavailable. Fallback triggered.");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Card Service is temporarily unavailable. Please try again later.");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Card Service");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @GetMapping("/transactions")
    @PostMapping("/transactions")
    public ResponseEntity<Map<String, Object>> transactionFallback() {
        log.error("Transaction Service is unavailable. Fallback triggered.");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Transaction Service is temporarily unavailable. Please try again later.");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Transaction Service");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}