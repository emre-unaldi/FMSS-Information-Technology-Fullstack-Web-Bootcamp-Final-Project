package unaldi.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import unaldi.authservice.utils.result.DataResult;
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
@Tag(name = "Authentication Controller", description = "User Authentication")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticate user credentials and generate JWT tokens",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User credentials for login",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = LoginRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "Login Example",
                                            summary = "User Login",
                                            description = "Complete request with username and password",
                                            value = "{\n"
                                                    + "  \"username\": \"emreunaldi\",\n"
                                                    + "  \"password\": \"unaldi38.\"\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<UserResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse login = authService.login(loginRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, login.getJwtCookie().toString())
                .header(HttpHeaders.SET_COOKIE, login.getJwtRefreshCookie().toString())
                .body(login.getUser());
    }

    @PostMapping("/logout")
    @Operation(
            summary = "User logout",
            description = "Clear user session and invalidate JWT tokens",
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User logout",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<Result> logout() {
        LogoutResponse logout = authService.logout();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, logout.getJwtCookie().toString())
                .header(HttpHeaders.SET_COOKIE, logout.getJwtRefreshCookie().toString())
                .body(logout.getResult());
    }

    @PostMapping("/refreshToken")
    @Operation(
            summary = "Refresh JWT token",
            description = "Refresh JWT token using refresh token stored in cookies",
            parameters = {
                    @Parameter(
                            name = "request",
                            description = "HTTP servlet request containing refresh token cookie"
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<Result> refreshToken(HttpServletRequest request) {
        RefreshTokenResponse refreshToken = authService.refreshToken(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshToken.getJwtCookie().toString())
                .body(refreshToken.getResult());
    }

}

