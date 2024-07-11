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
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPhotoFormatException extends RuntimeException {

    public InvalidPhotoFormatException(String message) {
        super(message);
    }

}