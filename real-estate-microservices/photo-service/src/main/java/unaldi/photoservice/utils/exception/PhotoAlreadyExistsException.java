package unaldi.photoservice.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class PhotoAlreadyExistsException extends RuntimeException {

    public PhotoAlreadyExistsException(String message) {
        super(message);
    }

}
