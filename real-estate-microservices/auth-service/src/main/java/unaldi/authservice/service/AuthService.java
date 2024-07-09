package unaldi.authservice.service;

import jakarta.servlet.http.HttpServletRequest;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.request.SignupRequest;
import unaldi.authservice.entity.dto.response.*;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
public interface AuthService {
    AuthenticationResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest);
    LogoutResponse logoutUser();
    RefreshTokenResponse refreshToken(HttpServletRequest request);
    List<UserInfoResponse> findAll();
    UserInfoResponse findById(Long userId);
}
