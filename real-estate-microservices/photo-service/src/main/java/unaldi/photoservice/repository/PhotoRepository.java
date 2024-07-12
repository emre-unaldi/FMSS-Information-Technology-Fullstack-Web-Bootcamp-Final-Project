package unaldi.photoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unaldi.photoservice.entity.Photo;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {

    @Query("SELECT EXISTS(SELECT 1 FROM Photo photo WHERE photo.name = :name)")
    Boolean existsByPhotoName(@Param("name") String name);

    @Query("SELECT photo FROM Photo photo WHERE photo.id IN :photoIds")
    List<Photo> findByPhotoIds(@Param("photoIds") List<String> photoIds);

}
