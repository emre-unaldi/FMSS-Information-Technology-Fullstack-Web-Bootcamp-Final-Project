package unaldi.advertservice.entity.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
 * @since 13.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressSaveRequest {

    @NotBlank
    @Size(max = 100)
    private String neighborhood;

    @NotBlank
    @Size(max = 100)
    private String street;

    @NotBlank
    @Size(max = 50)
    private String province;

    @NotBlank
    @Size(max = 50)
    private String county;

    @NotBlank
    @Size(max = 20)
    private String zipCode;

}
