package unaldi.userservice.utils.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessages {

    public static final String USER_NOT_FOUND = "User not found in database";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token not found in database";
    public static final String ROLE_NOT_FOUND = "User role not found in database";
    public static final String USERNAME_ALREADY_TAKEN = "User already exists with this username";
    public static final String EMAIL_ALREADY_TAKEN = "User already exists with this email";
    public static final String BAD_REQUEST = "The request could not be fulfilled";

}
