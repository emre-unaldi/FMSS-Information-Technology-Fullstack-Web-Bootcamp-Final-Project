package unaldi.userservice.utils.rabbitMQ.dto;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDTO {
    private Long id;
    private Long userId;
    private Integer packageCount;
    private Double price;
    private Double totalPrice;
    private LocalDate orderDate;
    private LocalDate expirationDate;
}

