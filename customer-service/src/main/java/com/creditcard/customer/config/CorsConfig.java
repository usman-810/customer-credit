//package com.creditcard.customer.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        
//        // Use specific origin instead of pattern to avoid duplicates
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//        
//        // Or for multiple origins:
//        // config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3001"));
//        
//        config.setAllowedHeaders(Arrays.asList("*"));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        config.setMaxAge(3600L);
//        
//        // Register for all paths
//        source.registerCorsConfiguration("/**", config);
//        
//        return new CorsFilter(source);
//    }
//}