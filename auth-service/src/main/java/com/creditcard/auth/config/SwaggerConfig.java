package com.creditcard.auth.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port}")
    private int serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Local Development Server");

        Server gatewayServer = new Server();
        gatewayServer.setUrl("http://localhost:8080");
        gatewayServer.setDescription("API Gateway");

        Contact contact = new Contact();
        contact.setName("Credit Card Portal Team");
        contact.setEmail("support@creditcard.com");
        contact.setUrl("https://github.com/yourusername/credit-card-portal");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Authentication Service API")
                .version("1.0.0")
                .description("REST API for User Authentication and JWT Token Management")
                .contact(contact)
                .license(license);

        // JWT Security Scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("JWT token for authentication. Obtain from /api/auth/login");

        Components components = new Components()
                .addSecuritySchemes("bearer-jwt", securityScheme);

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearer-jwt");

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, gatewayServer))
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}