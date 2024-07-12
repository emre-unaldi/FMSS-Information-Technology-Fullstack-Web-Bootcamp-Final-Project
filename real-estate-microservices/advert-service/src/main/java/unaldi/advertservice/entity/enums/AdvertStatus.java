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
public enum AdvertStatus {

    ACTIVE("Active"),
    PASSIVE("Passive"),
    IN_REVIEW("In Review");

    private final String message;

}
