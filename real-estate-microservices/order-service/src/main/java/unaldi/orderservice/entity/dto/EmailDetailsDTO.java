package unaldi.orderservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetailsDTO {

    private String recipient;
    private String body;
    private String subject;

}
