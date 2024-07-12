package unaldi.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.response.*;
import unaldi.authservice.service.AuthService;
import unaldi.authservice.utils.exception.RefreshTokenException;
import unaldi.authservice.utils.result.DataResult;
import unaldi.authservice.utils.result.ErrorResult;
import unaldi.authservice.utils.result.Result;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 10.07.2024
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<DataResult<UserResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse login = authService.login(loginRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, login.getJwtCookie().toString())
                .header(HttpHeaders.SET_COOKIE, login.getJwtRefreshCookie().toString())
                .body(login.getUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<Result> logout() {
        LogoutResponse logout = authService.logout();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, logout.getJwtCookie().toString())
                .header(HttpHeaders.SET_COOKIE, logout.getJwtRefreshCookie().toString())
                .body(logout.getResult());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Result> refreshToken(HttpServletRequest request) {
        try {
            RefreshTokenResponse refreshToken = authService.refreshToken(request);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, refreshToken.getJwtCookie().toString())
                    .body(refreshToken.getResult());

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(new ErrorResult(exception.getMessage()));
        } catch (RefreshTokenException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResult(exception.getMessage()));
        }
    }

}

