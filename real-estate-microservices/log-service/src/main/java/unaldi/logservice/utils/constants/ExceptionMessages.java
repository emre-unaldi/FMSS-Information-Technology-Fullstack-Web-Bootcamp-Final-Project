package unaldi.logservice.utils.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessages {

    public static final String LOG_NOT_FOUND = "Log record not found in database";
    public static final String BAD_REQUEST = "The request could not be fulfilled";

}