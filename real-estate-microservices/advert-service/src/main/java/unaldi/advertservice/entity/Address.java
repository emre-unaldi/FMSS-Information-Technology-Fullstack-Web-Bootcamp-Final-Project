package unaldi.advertservice.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "neighborhood", nullable = false, length = 100)
    private String neighborhood;

    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "province", nullable = false, length = 50)
    private String province;

    @Column(name = "county", nullable = false, length = 50)
    private String county;

    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @OneToOne(mappedBy = "address")
    private Advert advert;

}
