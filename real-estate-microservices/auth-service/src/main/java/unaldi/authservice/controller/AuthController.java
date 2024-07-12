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
    public ResponseEntity<UserInfoResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authentication = authService.login(loginRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, authentication.getJwtCookie().toString())
                .header(HttpHeaders.SET_COOKIE, authentication.getJwtRefreshCookie().toString())
                .body(authentication.getUserInfoResponse());
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser() {
        LogoutResponse logout = authService.logout();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, logout.getJwtCookie().toString())
                .header(HttpHeaders.SET_COOKIE, logout.getJwtRefreshCookie().toString())
                .body(logout.getMessageResponse());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<MessageResponse> refreshToken(HttpServletRequest request) {
        try {
            RefreshTokenResponse refreshToken = authService.refreshToken(request);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, refreshToken.getJwtCookie().toString())
                    .body(refreshToken.getMessageResponse());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (RefreshTokenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(e.getMessage()));
        }
    }

}

