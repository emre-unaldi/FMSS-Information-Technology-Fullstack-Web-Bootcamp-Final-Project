package unaldi.orderservice.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unaldi.orderservice.utils.client.dto.response.UserResponse;

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
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private UserResponse user;
    private Integer packageCount;
    private Double price;
    private Double totalPrice;
    private LocalDate orderDate;
    private LocalDate expirationDate;
}