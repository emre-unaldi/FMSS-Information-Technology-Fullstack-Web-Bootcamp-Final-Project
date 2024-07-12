package unaldi.authservice.utils.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessages {

    public static final String REFRESH_TOKEN_EMPTY = "Refresh token is empty";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token is not in database";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token was expired. Please make a new signin request";
    public static final String USERNAME_NOT_FOUND = "User Not Found with username: ";
    public static final String USER_NOT_FOUND = "User not found in database";
    public static final String USER_UNAUTHORIZED = "Unauthorized user request denied";
    public static final String BAD_REQUEST = "Your request is not valid. Please try to correct the format";

}
