package unaldi.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unaldi.userservice.entity.RefreshToken;

import java.util.Optional;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT refreshToken FROM RefreshToken refreshToken WHERE refreshToken.user.id = :userId")
    Optional<RefreshToken> findByUserId(@Param("userId") Long userId);

}
