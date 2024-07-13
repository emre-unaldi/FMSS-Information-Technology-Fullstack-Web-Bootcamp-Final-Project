package unaldi.advertservice.entity.dto.request;

import jakarta.validation.constraints.*;
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
@AllArgsConstructor
@NoArgsConstructor
public class AdvertSaveRequest {

    @NotNull
    private Set<String> photoIds;

    @NotNull
    private Long userId;

    @NotNull
    private HousingType housingType;

    @NotNull
    private AdvertStatus advertStatus;

    @NotNull
    private AdvertType advertType;

    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @NotBlank
    @Size(min = 10, max = 250)
    private String description;

    @NotNull
    private Long addressId;

    @NotNull
    @FutureOrPresent
    private LocalDate releaseDate;

    @NotNull
    @Future
    private LocalDate validityDate;

    @NotNull
    private Integer area;

    @NotNull
    @Min(1)
    private Integer numberOfRooms;

    @NotNull
    private Long price;

    @NotNull
    private Boolean isBalcony;

    @NotNull
    private Boolean isCarPark;

}
