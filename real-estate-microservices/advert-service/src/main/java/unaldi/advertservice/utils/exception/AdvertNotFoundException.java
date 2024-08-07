package unaldi.advertservice.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdvertNotFoundException extends RuntimeException {

    public AdvertNotFoundException(String message) {
        super(message);
    }

}
