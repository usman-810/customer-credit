package com.creditcard.auth.controller;

import com.creditcard.auth.dto.ApiResponse;
import com.creditcard.auth.dto.AuthResponse;
import com.creditcard.auth.dto.LoginRequest;
import com.creditcard.auth.dto.RegisterRequest;
import com.creditcard.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and Authorization APIs")
// ❌ REMOVED: @CrossOrigin(origins = "*")  // Gateway handles CORS
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "Register new user",
        description = "Register a new user account"
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        
        log.info("REST request to register user: {}", request.getUsername());
        
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "User registered successfully"));
    }

    @Operation(
        summary = "User login",
        description = "Authenticate user and get JWT token"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        
        log.info("REST request to login user: {}", request.getUsername());
        
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @Operation(
        summary = "Refresh token",
        description = "Get a new access token using refresh token"
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Parameter(description = "Refresh token") 
            @RequestHeader("Refresh-Token") String refreshToken) {
        
        log.info("REST request to refresh token");
        
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(response, "Token refreshed successfully"));
    }

    @Operation(
        summary = "Logout",
        description = "Logout user and invalidate token"
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Parameter(description = "Username") 
            @RequestParam String username) {
        
        log.info("REST request to logout user: {}", username);
        
        authService.logout(username);
        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful"));
    }

    @Operation(
        summary = "Validate token",
        description = "Check if a JWT token is valid"
    )
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(
            @Parameter(description = "JWT token") 
            @RequestHeader("Authorization") String token) {
        
        log.info("REST request to validate token");
        
        // Remove "Bearer " prefix if present
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        
        boolean isValid = authService.validateToken(jwtToken);
        return ResponseEntity.ok(ApiResponse.success(isValid, 
            isValid ? "Token is valid" : "Token is invalid"));
    }
}