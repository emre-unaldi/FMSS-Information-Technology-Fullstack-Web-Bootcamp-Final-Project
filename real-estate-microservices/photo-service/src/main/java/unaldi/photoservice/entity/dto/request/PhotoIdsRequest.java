package unaldi.photoservice.entity.dto.request;

import lombok.Getter;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
@Getter
public class PhotoIdsRequest {
    private List<String> photoIds;
}
