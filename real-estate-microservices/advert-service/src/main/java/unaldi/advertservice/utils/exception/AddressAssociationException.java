package unaldi.advertservice.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AddressAssociationException extends RuntimeException {

    public AddressAssociationException(String message) {
        super(message);
    }

}
