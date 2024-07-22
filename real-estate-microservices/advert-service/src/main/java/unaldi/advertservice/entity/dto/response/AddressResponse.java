package unaldi.advertservice.entity.dto.response;

import lombok.*;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AddressResponse {
    private Long id;
    private String neighborhood;
    private String street;
    private String province;
    private String county;
    private String zipCode;
}
