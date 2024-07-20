package unaldi.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.request.TokenValidRequest;
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
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
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
        DataResult<UserResponse> userResponse = authService.login(loginRequest);

        System.out.println("JWT Cookie: " + userResponse.getData().getAccessToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userResponse.getData().getAccessToken())
                .body(userResponse);
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
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.logout());
    }

    @PostMapping("/verifyToken")
    @Operation(
            summary = "Verify JWT token",
            description = "Verify the validity of a JWT token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JWT token to be verified",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = TokenValidRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "Verify Token Example",
                                            summary = "Token Verification",
                                            description = "Complete request with JWT token",
                                            value = "{\n"
                                                    + "  \"accessToken\": \"your-jwt-token\"\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<Boolean>> verifyToken(@RequestBody TokenValidRequest tokenValidRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.verifyToken(tokenValidRequest.getAccessToken()));
    }

}

