package unaldi.authservice.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String phoneNumber;
	private List<String> roles;

}
