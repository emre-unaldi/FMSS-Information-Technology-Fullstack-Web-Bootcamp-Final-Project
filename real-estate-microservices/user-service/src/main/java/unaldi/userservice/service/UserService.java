package unaldi.userservice.service;

import unaldi.userservice.entity.dto.request.SignUpRequest;
import unaldi.userservice.entity.dto.response.UserResponse;
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
public interface UserService {

    DataResult<UserResponse> register(SignUpRequest signUpRequest);
    DataResult<List<UserResponse>> findAll();
    DataResult<UserResponse> findById(Long userId);
    Result deleteById(Long userId);

}