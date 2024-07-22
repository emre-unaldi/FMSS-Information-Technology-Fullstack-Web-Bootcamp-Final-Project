package unaldi.orderservice.utils.client.dto.response;

import lombok.*;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {
    private boolean success;
    private String message;
    private String responseDateTime;
    private T data;
}