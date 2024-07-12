package unaldi.advertservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unaldi.advertservice.entity.Address;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

}
