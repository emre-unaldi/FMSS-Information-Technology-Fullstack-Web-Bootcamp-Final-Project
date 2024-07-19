package unaldi.authservice.service;

import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.response.*;
import unaldi.authservice.utils.result.DataResult;
import unaldi.authservice.utils.result.Result;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
public interface AuthService {

    DataResult<UserResponse> login(LoginRequest loginRequest);
    Result logout();

}
