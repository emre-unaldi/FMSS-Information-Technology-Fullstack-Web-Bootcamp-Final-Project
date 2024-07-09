package unaldi.authservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 2.07.2024
 */
@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(min = 3, max = 50)
  @Column(name = "first_name", nullable = false, length = 30)
  private String firstName;

  @NotBlank
  @Size(min = 3, max = 50)
  @Column(name = "last_name", nullable = false, length = 30)
  private String lastName;

  @NotBlank
  @Size(max = 20)
  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  @Column(name = "email", nullable = false, unique = true, length = 50)
  private String email;

  @NotBlank
  @Size(max = 120)
  @Column(name = "password", nullable = false, length = 120)
  private String password;

  @NotBlank
  @Size(max = 20)
  @Column(name = "phone_number", nullable = false, length = 20)
  private String phoneNumber;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", 
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
