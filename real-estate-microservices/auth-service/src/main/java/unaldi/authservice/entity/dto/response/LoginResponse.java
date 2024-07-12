package unaldi.authservice.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseCookie;
import unaldi.authservice.utils.result.DataResult;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private DataResult<UserResponse> user;
    private ResponseCookie jwtCookie;
    private ResponseCookie jwtRefreshCookie;

}
