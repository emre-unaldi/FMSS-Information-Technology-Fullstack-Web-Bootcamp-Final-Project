package unaldi.userservice.service.Impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unaldi.userservice.entity.ERole;
import unaldi.userservice.entity.RefreshToken;
import unaldi.userservice.entity.Role;
import unaldi.userservice.entity.User;
import unaldi.userservice.entity.dto.request.SignUpRequest;
import unaldi.userservice.entity.dto.request.UserUpdateRequest;
import unaldi.userservice.entity.dto.response.UserResponse;
import unaldi.userservice.repository.RefreshTokenRepository;
import unaldi.userservice.repository.RoleRepository;
import unaldi.userservice.repository.UserRepository;
import unaldi.userservice.service.UserService;
import unaldi.userservice.service.mapper.UserMapper;
import unaldi.userservice.utils.constants.ExceptionMessages;
import unaldi.userservice.utils.constants.Messages;
import unaldi.userservice.utils.exception.*;
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
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
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
        Set<Role> roles = mapToUserRoles(strRoles);

        userDto.setRoles(roles);
        User user = userRepository.save(userDto);

        return new SuccessDataResult<>(
                UserMapper.INSTANCE.userToUserResponse(user),
                Messages.USER_REGISTERED
        );
    }

    @Override
    public DataResult<UserResponse> update(UserUpdateRequest userUpdateRequest) {
        if (!userRepository.existsById(userUpdateRequest.getId())) {
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }

        if (userRepository.existsByUsername(userUpdateRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(ExceptionMessages.USERNAME_ALREADY_TAKEN);
        }

        if (userRepository.existsByEmail(userUpdateRequest.getEmail())) {
            throw new EmailAlreadyExistsException(ExceptionMessages.EMAIL_ALREADY_TAKEN);
        }

        String encodedPassword = passwordEncoder.encode(userUpdateRequest.getPassword());
        User userDto = UserMapper.INSTANCE.userUpdateRequestToUser(userUpdateRequest, encodedPassword);

        Set<String> strRoles = userUpdateRequest.getRoles();
        Set<Role> roles = mapToUserRoles(strRoles);

        userDto.setRoles(roles);
        User user = userRepository.save(userDto);

        return new SuccessDataResult<>(
                UserMapper.INSTANCE.userToUserResponse(user),
                Messages.USER_UPDATED
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

    @Transactional
    @Override
    public Result deleteById(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        RefreshToken refreshToken = refreshTokenRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ExceptionMessages.REFRESH_TOKEN_NOT_FOUND));

        refreshTokenRepository.deleteById(refreshToken.getId());
        userRepository.deleteById(user.getId());

        return new SuccessResult(Messages.USER_DELETED);
    }

    private Set<Role> mapToUserRoles(Set<String> strRoles) {
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

        return roles;
    }
}
