package com.creditcard.customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customerServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Service API")
                        .description("REST API for Customer Management in PWC Credit Card Portal")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PWC Credit Card Team")
                                .email("support@creditcard.com")
                                .url("https://github.com/pwc/credit-card-portal"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(Arrays.asList(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Customer Service - Local Development"),
                        new Server()
                                .url("http://localhost:8080/api/customers")
                                .description("Customer Service - API Gateway")));
    }
}