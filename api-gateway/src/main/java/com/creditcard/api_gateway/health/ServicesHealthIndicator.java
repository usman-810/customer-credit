package com.creditcard.api_gateway.health;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ServicesHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Health health() {
        Map<String, String> services = new HashMap<>();
        boolean allHealthy = true;

        // Check Customer Service
        try {
            restTemplate.getForEntity("http://localhost:8081/actuator/health", String.class);
            services.put("customer-service", "UP");
        } catch (Exception e) {
            services.put("customer-service", "DOWN");
            allHealthy = false;
        }

        // Check Card Service
        try {
            restTemplate.getForEntity("http://localhost:8082/actuator/health", String.class);
            services.put("card-service", "UP");
        } catch (Exception e) {
            services.put("card-service", "DOWN");
            allHealthy = false;
        }

        // Check Transaction Service
        try {
            restTemplate.getForEntity("http://localhost:8083/actuator/health", String.class);
            services.put("transaction-service", "UP");
        } catch (Exception e) {
            services.put("transaction-service", "DOWN");
            allHealthy = false;
        }

        if (allHealthy) {
            return Health.up().withDetails(services).build();
        } else {
            return Health.down().withDetails(services).build();
        }
    }
}