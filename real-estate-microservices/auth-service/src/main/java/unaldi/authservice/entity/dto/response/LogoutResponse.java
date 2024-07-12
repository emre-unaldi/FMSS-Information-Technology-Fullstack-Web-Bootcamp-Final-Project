package unaldi.authservice.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseCookie;
import unaldi.authservice.utils.result.Result;

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
public class LogoutResponse {

    private Result result;
    private ResponseCookie jwtCookie;
    private ResponseCookie jwtRefreshCookie;

}
