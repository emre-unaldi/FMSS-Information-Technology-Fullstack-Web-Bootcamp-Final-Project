package unaldi.advertservice.utils.client.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Getter
@Builder
public class PhotoIdsRequest {
    private List<String> photoIds;
}
