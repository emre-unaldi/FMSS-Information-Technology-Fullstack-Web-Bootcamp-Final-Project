package unaldi.advertservice.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Getter
@AllArgsConstructor
public enum AdvertType {

    FOR_SALE("For Sale"),
    FOR_SENT("For Sent");

    private final String message;

}
