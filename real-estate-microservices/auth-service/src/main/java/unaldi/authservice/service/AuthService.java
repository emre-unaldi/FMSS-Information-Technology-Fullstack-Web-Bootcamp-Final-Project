package unaldi.authservice.service;

import jakarta.servlet.http.HttpServletRequest;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.response.*;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);
    LogoutResponse logout();
    RefreshTokenResponse refreshToken(HttpServletRequest request);

}
