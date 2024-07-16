package unaldi.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Entity
@Table(name = "packages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "advert_count")
    private Integer advertCount;

    @Column(name = "package_time")
    private Long packageTime;

    @Column(name = "price")
    private Double price;

}
