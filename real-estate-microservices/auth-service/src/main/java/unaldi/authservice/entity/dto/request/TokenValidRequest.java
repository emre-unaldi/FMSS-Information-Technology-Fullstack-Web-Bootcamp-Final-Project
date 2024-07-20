package unaldi.authservice.entity.dto.request;

import lombok.*;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 20.07.2024
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidRequest {
    private String accessToken;
}
