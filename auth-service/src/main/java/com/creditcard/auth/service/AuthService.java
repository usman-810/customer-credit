package com.creditcard.auth.service;

import com.creditcard.auth.dto.AuthResponse;
import com.creditcard.auth.dto.LoginRequest;
import com.creditcard.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);

    void logout(String username);

    boolean validateToken(String token);
}