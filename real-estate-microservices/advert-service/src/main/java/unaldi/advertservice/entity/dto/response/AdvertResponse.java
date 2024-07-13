package unaldi.advertservice.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unaldi.advertservice.entity.enums.AdvertStatus;
import unaldi.advertservice.entity.enums.AdvertType;
import unaldi.advertservice.entity.enums.HousingType;

import java.time.LocalDate;
import java.util.Set;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertResponse {
    private Long id;
    private String advertNumber;
    private Set<String> photoIds;
    private Long userId;
    private HousingType housingType;
    private AdvertStatus advertStatus;
    private AdvertType advertType;
    private String title;
    private String description;
    private Long addressId;
    private LocalDate releaseDate;
    private LocalDate validityDate;
    private Integer area;
    private Integer numberOfRooms;
    private Long price;
    private Boolean isBalcony;
    private Boolean isCarPark;
}
