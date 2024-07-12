package unaldi.userservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unaldi.userservice.entity.ERole;
import unaldi.userservice.entity.Role;
import unaldi.userservice.entity.User;
import unaldi.userservice.entity.dto.request.SignUpRequest;
import unaldi.userservice.entity.dto.response.UserResponse;
import unaldi.userservice.repository.RoleRepository;
import unaldi.userservice.repository.UserRepository;
import unaldi.userservice.service.UserService;
import unaldi.userservice.service.mapper.UserMapper;
import unaldi.userservice.utils.constants.ExceptionMessages;
import unaldi.userservice.utils.constants.Messages;
import unaldi.userservice.utils.exception.EmailAlreadyExistsException;
import unaldi.userservice.utils.exception.RoleNotFoundException;
import unaldi.userservice.utils.exception.UserNotFoundException;
import unaldi.userservice.utils.exception.UsernameAlreadyExistsException;
import unaldi.userservice.utils.result.DataResult;
import unaldi.userservice.utils.result.Result;
import unaldi.userservice.utils.result.SuccessDataResult;
import unaldi.userservice.utils.result.SuccessResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public DataResult<UserResponse> register(SignUpRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(ExceptionMessages.USERNAME_ALREADY_TAKEN);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyExistsException(ExceptionMessages.EMAIL_ALREADY_TAKEN);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        User userDto = UserMapper.INSTANCE.signUpRequestToUser(signUpRequest, encodedPassword);

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));

            roles.add(userRole);

        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository
                                .findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));

                        roles.add(adminRole);

                        break;
                    case "moderator":
                        Role modRole = roleRepository
                                .findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));

                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository
                                .findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND));

                        roles.add(userRole);
                }
            });
        }

        userDto.setRoles(roles);
        User user = userRepository.save(userDto);

        return new SuccessDataResult<>(
                UserMapper.INSTANCE.userToUserResponse(user),
                Messages.USER_REGISTERED
        );
    }

    @Override
    public DataResult<List<UserResponse>> findAll() {
        List<User> users = userRepository.findAll();

        return new SuccessDataResult<>(
                UserMapper.INSTANCE.usersToUserResponses(users),
                Messages.USERS_LISTED
        );
    }

    @Override
    public DataResult<UserResponse> findById(Long userId) {
        UserResponse userResponse = userRepository
                .findById(userId)
                .map(UserMapper.INSTANCE::userToUserResponse)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        return new SuccessDataResult<>(userResponse, Messages.USER_FOUND);
    }

    @Override
    public Result deleteById(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        this.userRepository.deleteById(user.getId());

        return new SuccessResult(Messages.USER_DELETED);
    }
}
