package unaldi.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.request.SignupRequest;
import unaldi.authservice.entity.dto.response.*;
import unaldi.authservice.service.AuthService;
import unaldi.authservice.utils.exception.RefreshTokenException;

import java.util.List;

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

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authentication = authService.authenticateUser(loginRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, authentication.getJwtCookie().toString())
                .header(HttpHeaders.SET_COOKIE, authentication.getJwtRefreshCookie().toString())
                .body(authentication.getUserInfoResponse());
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.registerUser(signUpRequest));
    }

    @PostMapping("/signout")
    public ResponseEntity<MessageResponse> logoutUser() {
        LogoutResponse logout = authService.logoutUser();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, logout.getJwtCookie().toString())
                .header(HttpHeaders.SET_COOKIE, logout.getJwtRefreshCookie().toString())
                .body(logout.getMessageResponse());
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<MessageResponse> refreshtoken(HttpServletRequest request) {
        try {
            RefreshTokenResponse refreshTokenResponse = authService.refreshToken(request);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, refreshTokenResponse.getJwtCookie().toString())
                    .body(refreshTokenResponse.getMessageResponse());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (RefreshTokenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserInfoResponse>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.findAll());
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponse> findById(@PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.findById(userId));
    }
}

