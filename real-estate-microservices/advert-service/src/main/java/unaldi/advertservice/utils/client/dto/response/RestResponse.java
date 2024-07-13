package unaldi.advertservice.utils.client.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Getter
@Setter
@Builder
public class RestResponse<T> {
    private boolean success;
    private String message;
    private String responseDateTime;
    private T data;
}