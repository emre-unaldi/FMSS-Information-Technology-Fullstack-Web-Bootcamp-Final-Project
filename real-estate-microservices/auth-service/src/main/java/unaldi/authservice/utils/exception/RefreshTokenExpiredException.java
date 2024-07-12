package unaldi.authservice.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshTokenExpiredException extends RuntimeException {

    public RefreshTokenExpiredException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }

}
