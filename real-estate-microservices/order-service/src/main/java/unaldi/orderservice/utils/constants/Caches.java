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
public class Caches {

    public static final String ORDER_CACHE = "order";
    public static final String ORDERS_CACHE = "orders";
    public static final String KEY_PREFIX = "order-service:";

}