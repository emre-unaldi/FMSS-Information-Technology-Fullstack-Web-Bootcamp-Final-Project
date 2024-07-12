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
public enum HousingType {

    APARTMENT("Apartment"),
    RESIDENCE("Residence"),
    DETACHED_HOUSE("Detached House"),
    VILLA("Villa"),
    SUMMER_HOUSE("Summer House");

    private final String message;

}
