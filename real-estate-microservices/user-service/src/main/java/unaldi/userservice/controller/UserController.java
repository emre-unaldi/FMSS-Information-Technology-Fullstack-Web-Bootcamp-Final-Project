package unaldi.userservice.controller;

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
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<DataResult<UserResponse>> register(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(signUpRequest));
    }

    @PutMapping
    public ResponseEntity<DataResult<UserResponse>> update(@RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.update(userUpdateRequest));
    }

    @GetMapping
    public ResponseEntity<DataResult<List<UserResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DataResult<UserResponse>> findById(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Result> deleteById(@PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.deleteById(userId));
    }

}
