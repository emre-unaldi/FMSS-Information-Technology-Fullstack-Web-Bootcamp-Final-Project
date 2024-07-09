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
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RefreshTokenEmptyException extends RuntimeException {

    public RefreshTokenEmptyException(String message) {
        super(message);
    }

}
