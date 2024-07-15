package unaldi.advertservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unaldi.advertservice.entity.Advert;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    @Query("SELECT EXISTS(SELECT 1 FROM Advert advert WHERE advert.address.id = :addressId)")
    Boolean existsByAddress(@Param("addressId") Long addressId);

}
