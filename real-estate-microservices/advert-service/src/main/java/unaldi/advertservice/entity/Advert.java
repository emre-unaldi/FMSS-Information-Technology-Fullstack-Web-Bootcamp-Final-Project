package unaldi.advertservice.entity;

import jakarta.persistence.*;
import lombok.*;
import unaldi.advertservice.entity.enums.AdvertStatus;
import unaldi.advertservice.entity.enums.AdvertType;
import unaldi.advertservice.entity.enums.HousingType;

import java.time.LocalDate;
import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@Entity
@Table(name = "adverts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Advert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "advert_number", nullable = false, unique = true)
    private String advertNumber;

    @ElementCollection
    @CollectionTable(
            name = "advert_photos",
            joinColumns = @JoinColumn(name = "advert_id")
    )
    @Column(name = "photo_id", nullable = false)
    private List<String> photoIds;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "housing_type")
    private HousingType housingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "advert_status")
    private AdvertStatus advertStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "advert_type")
    private AdvertType advertType;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Address address;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "validity_date", nullable = false)
    private LocalDate validityDate;

    @Column(name = "area", nullable = false)
    private Integer area;

    @Column(name = "number_of_rooms", nullable = false)
    private Integer numberOfRooms;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "is_balcony", nullable = false)
    private Boolean isBalcony;

    @Column(name = "is_car_park", nullable = false)
    private Boolean isCarPark;

}
