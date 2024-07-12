package unaldi.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT user FROM User user WHERE user.username = :username")
  Optional<User> findByUsername(@Param("username") String username);

}
