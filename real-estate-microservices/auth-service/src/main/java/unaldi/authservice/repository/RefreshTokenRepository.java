package unaldi.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unaldi.authservice.entity.RefreshToken;
import unaldi.authservice.entity.User;

import java.util.Optional;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT refreshToken FROM RefreshToken refreshToken WHERE refreshToken.token = :token")
    Optional<RefreshToken> findByToken(@Param("token") String token);

    @Modifying
    void deleteByUser(User user);

}
