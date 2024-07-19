package unaldi.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.userservice.entity.dto.request.SignUpRequest;
import unaldi.userservice.entity.dto.request.UserUpdateRequest;
import unaldi.userservice.entity.dto.response.UserResponse;
import unaldi.userservice.service.UserService;
import unaldi.userservice.utils.result.DataResult;
import unaldi.userservice.utils.result.Result;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/users")
@Tag(name="User Controller", description = "User Management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(
            description = "Register new user",
            summary = "Register a new user",
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User Infos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SignUpRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "New User to Register",
                                            summary = "New Register",
                                            description = "Complete the request with all available fields to register a new user",
                                            value = "{\n"
                                                    + "  \"firstName\": \"Emre\",\n"
                                                    + "  \"lastName\": \"Ünaldı\",\n"
                                                    + "  \"username\": \"emreunaldi\",\n"
                                                    + "  \"email\": \"emre.unaldi@gmail.com\",\n"
                                                    + "  \"password\": \"unaldi38.\",\n"
                                                    + "  \"phoneNumber\": \"+5078711189\",\n"
                                                    + "  \"roles\": [\"user\", \"admin\"]\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<UserResponse>> register(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(signUpRequest));
    }

    @PutMapping
    @Operation(
            description = "Update user",
            summary = "Update a user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User Infos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserUpdateRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "User",
                                            summary = "Update",
                                            description = "Update the information of a registered user",
                                            value = "{\n"
                                                    + "  \"id\": 1,\n"
                                                    + "  \"firstName\": \"Emre\",\n"
                                                    + "  \"lastName\": \"Ünaldı\",\n"
                                                    + "  \"username\": \"emreunaldi\",\n"
                                                    + "  \"email\": \"emre.unaldi@gmail.com\",\n"
                                                    + "  \"password\": \"unaldi38.\",\n"
                                                    + "  \"phoneNumber\": \"+5078710000\",\n"
                                                    + "  \"roles\": [\"user\", \"admin\"]\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<UserResponse>> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.update(userUpdateRequest));
    }

    @GetMapping
    @Operation(
            description = "Find all registered user",
            summary = "Find all",
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User Infos",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<List<UserResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findAll());
    }

    @GetMapping("/{userId}")
    @Operation(
            description = "Find with id a user",
            summary = "Find by id",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "Id of the user to retrieve",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<UserResponse>> findById(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findById(userId));
    }

    @GetMapping("/findByUsername/{username}")
    @Operation(
            description = "Find with username a user",
            summary = "Find by username",
            parameters = {
                    @Parameter(
                            name = "username",
                            description = "Username of the user to retrieve",
                            required = true,
                            example = "unaldi",
                            schema = @Schema(type = "string")
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User username",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<UserResponse>> findByUsername(@PathVariable("username") String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findByUsername(username));
    }

    @DeleteMapping("/{userId}")
    @Operation(
            description = " Delete by id a user",
            summary = "Delete a user",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "Id of the user to retrieve",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<Result> deleteById(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.deleteById(userId));
    }

}
