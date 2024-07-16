package unaldi.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unaldi.orderservice.entity.Package;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {



}
