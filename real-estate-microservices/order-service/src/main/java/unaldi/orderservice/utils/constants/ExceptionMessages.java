package unaldi.orderservice.utils.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessages {

    public static final String ORDER_NOT_FOUND = "Order not found in database";
    public static final String USER_NOT_FOUND = "User not found in database";
    public static final String BAD_REQUEST = "The request could not be fulfilled";

}
