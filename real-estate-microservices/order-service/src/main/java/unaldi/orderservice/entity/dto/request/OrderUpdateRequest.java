package unaldi.orderservice.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class OrderUpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @Size(min = 1, max = 10)
    @NotNull
    private Integer packageCount;

    @NotNull
    private Integer price;

}