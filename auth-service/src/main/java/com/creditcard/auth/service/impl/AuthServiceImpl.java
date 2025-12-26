package com.creditcard.auth.service.impl;

import com.creditcard.auth.dto.AuthResponse;
import com.creditcard.auth.dto.LoginRequest;
import com.creditcard.auth.dto.RegisterRequest;
import com.creditcard.auth.entity.User;
import com.creditcard.auth.exception.*;
import com.creditcard.auth.repositoty.UserRepository;
import com.creditcard.auth.service.AuthService;
import com.creditcard.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 30;

    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setIsActive(true);
        user.setIsEmailVerified(false);
        user.setFailedLoginAttempts(0);

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        // Generate tokens
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getUsername());
        Date expiresAt = jwtUtil.extractExpiration(token);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresAt(expiresAt.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                .user(mapToUserDTO(savedUser))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + request.getUsername()));

        // Check if account is locked
        if (user.isAccountLocked()) {
            log.warn("Login attempt on locked account: {}", request.getUsername());
            throw new AccountLockedException(
                "Account is locked until " + user.getAccountLockedUntil() + 
                ". Please try again later or contact support."
            );
        }

        // Check if account is active
        if (!user.getIsActive()) {
            throw new InvalidCredentialsException("Account is inactive. Please contact support.");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            handleFailedLogin(user);
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // Successful login
        handleSuccessfulLogin(user);

        // Generate tokens
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        Date expiresAt = jwtUtil.extractExpiration(token);

        log.info("User logged in successfully: {}", user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresAt(expiresAt.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                .user(mapToUserDTO(user))
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Refreshing token");

        try {
            String username = jwtUtil.extractUsername(refreshToken);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

            if (jwtUtil.validateToken(refreshToken, username)) {
                String newToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
                String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());
                Date expiresAt = jwtUtil.extractExpiration(newToken);

                log.info("Token refreshed successfully for user: {}", username);

                return AuthResponse.builder()
                        .token(newToken)
                        .refreshToken(newRefreshToken)
                        .expiresAt(expiresAt.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                        .user(mapToUserDTO(user))
                        .build();
            } else {
                throw new InvalidCredentialsException("Invalid refresh token");
            }
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            throw new InvalidCredentialsException("Invalid refresh token");
        }
    }

    @Override
    public void logout(String username) {
        log.info("Logout for user: {}", username);
        // In a production system, you would invalidate the token here
        // For now, we'll just log the logout
        // You could store invalidated tokens in Redis with TTL
    }

    @Override
    public boolean validateToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            return jwtUtil.validateToken(token, username);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private void handleFailedLogin(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
            log.warn("Account locked due to multiple failed login attempts: {}", user.getUsername());
        }

        userRepository.save(user);
    }

    private void handleSuccessfulLogin(User user) {
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    private AuthResponse.UserDTO mapToUserDTO(User user) {
        return AuthResponse.UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}