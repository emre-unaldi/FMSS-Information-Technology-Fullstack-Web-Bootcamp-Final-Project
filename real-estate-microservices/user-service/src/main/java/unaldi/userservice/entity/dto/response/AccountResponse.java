package unaldi.userservice.entity.dto.response;

import lombok.*;

import java.time.LocalDate;

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
public class AccountResponse {
    private Long id;
    private Long userId;
    private Integer advertCount;
    private LocalDate expirationDate;
    private Boolean isSubscribe;
}
