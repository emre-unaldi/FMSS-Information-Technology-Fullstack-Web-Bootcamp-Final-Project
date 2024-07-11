package unaldi.photoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unaldi.photoservice.entity.Photo;

import java.util.UUID;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
public interface PhotoRepository extends JpaRepository<Photo, String> {

    @Query("SELECT CASE WHEN (SELECT COUNT(photo) FROM Photo photo WHERE photo.name = :name) > 0 THEN true ELSE false END")
    Boolean findByName(@Param("name") String name);

}
