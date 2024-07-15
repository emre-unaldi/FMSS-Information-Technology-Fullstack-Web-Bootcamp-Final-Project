package unaldi.advertservice.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unaldi.advertservice.entity.enums.AdvertStatus;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertStatusUpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private AdvertStatus advertStatus;

}

