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

    public static final String USERNAME_ALREADY_TAKEN = "Error: Username is already taken!";
    public static final String EMAIL_ALREADY_TAKEN = "Error: Email is already taken!";
    public static final String ROLE_NOT_FOUND = "Error: Role is not found.";
    public static final String REFRESH_TOKEN_EMPTY = "Error: Refresh token is empty!";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Error: Refresh token is not in database!";
    public static final String REFRESH_TOKEN_EXPIRED = "Error: Refresh token was expired. Please make a new signin request";
    public static final String USERNAME_NOT_FOUND = "User Not Found with username: ";
}
