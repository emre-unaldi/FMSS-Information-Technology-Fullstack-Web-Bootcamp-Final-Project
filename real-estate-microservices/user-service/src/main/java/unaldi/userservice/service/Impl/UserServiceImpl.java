package unaldi.userservice.service.Impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unaldi.userservice.entity.*;
import unaldi.userservice.entity.dto.request.AccountSaveRequest;
import unaldi.userservice.entity.dto.request.SignUpRequest;
import unaldi.userservice.entity.dto.request.UserUpdateRequest;
import unaldi.userservice.entity.dto.response.UserResponse;
import unaldi.userservice.repository.RefreshTokenRepository;
import unaldi.userservice.repository.RoleRepository;
import unaldi.userservice.repository.UserRepository;
import unaldi.userservice.service.AccountService;
import unaldi.userservice.service.UserService;
import unaldi.userservice.service.mapper.UserMapper;
import unaldi.userservice.utils.constants.Caches;
import unaldi.userservice.utils.constants.ExceptionMessages;
import unaldi.userservice.utils.constants.Messages;
import unaldi.userservice.utils.exception.*;
import unaldi.userservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.userservice.utils.rabbitMQ.enums.HttpRequestMethod;
import unaldi.userservice.utils.rabbitMQ.enums.LogType;
import unaldi.userservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.userservice.utils.result.DataResult;
import unaldi.userservice.utils.result.Result;
import unaldi.userservice.utils.result.SuccessDataResult;
import unaldi.userservice.utils.result.SuccessResult;

import java.time.LocalDateTime;
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
    private final LogProducer logProducer;
    private final AccountService accountService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository, LogProducer logProducer, AccountService accountService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.logProducer = logProducer;
        this.accountService = accountService;
    }

    @CacheEvict(value = Caches.USERS_CACHE, allEntries = true, condition = "#result.success != false")
    @Transactional
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

        Account account = accountService.save(AccountSaveRequest.builder().userId(user.getId()).build());
        user.setAccount(account);
        userRepository.save(user);

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.POST, Messages.USER_REGISTERED));

        return new SuccessDataResult<>(
                UserMapper.INSTANCE.userToUserResponse(user),
                Messages.USER_REGISTERED
        );
    }

    @Caching(
            put = { @CachePut(value = Caches.USER_CACHE, key = "#userUpdateRequest.getId()", unless = "#result.success != true") },
            evict = { @CacheEvict(value = Caches.USERS_CACHE, allEntries = true, condition = "#result.success != false") }
    )
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
        Account account = accountService.findByUserId(userUpdateRequest.getId());

        userDto.setRoles(roles);
        userDto.setAccount(account);
        User user = userRepository.save(userDto);

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.PUT, Messages.USER_UPDATED));

        return new SuccessDataResult<>(
                UserMapper.INSTANCE.userToUserResponse(user),
                Messages.USER_UPDATED
        );
    }

    @Cacheable(value = Caches.USERS_CACHE, key = "'all'", unless = "#result.success != true")
    @Override
    public DataResult<List<UserResponse>> findAll() {
        List<User> users = userRepository.findAll();

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.GET, Messages.USERS_LISTED));

        return new SuccessDataResult<>(
                UserMapper.INSTANCE.usersToUserResponses(users),
                Messages.USERS_LISTED
        );
    }

    @Cacheable(value = Caches.USER_CACHE, key = "#userId", unless = "#result.success != true")
    @Override
    public DataResult<UserResponse> findById(Long userId) {
        UserResponse userResponse = userRepository
                .findById(userId)
                .map(UserMapper.INSTANCE::userToUserResponse)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.GET, Messages.USER_FOUND));

        return new SuccessDataResult<>(userResponse, Messages.USER_FOUND);
    }

    @Cacheable(value = Caches.USER_CACHE, key = "#username", unless = "#result.success != true")
    @Override
    public DataResult<UserResponse> findByUsername(String username) {
        UserResponse userResponse = userRepository
                .findByUsername(username)
                .map(UserMapper.INSTANCE::userToUserResponse)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.GET, Messages.USER_FOUND));

        return new SuccessDataResult<>(userResponse, Messages.USER_FOUND);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = Caches.USERS_CACHE, allEntries = true, condition = "#result.success != false"),
                    @CacheEvict(value = Caches.USER_CACHE, key = "#userId", condition = "#result.success != false")
            }
    )
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

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.DELETE, Messages.USER_DELETED));

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

    private LogDTO prepareLogDTO(HttpRequestMethod httpRequestMethod, String message) {
        return LogDTO
                .builder()
                .serviceName("user-service")
                .httpRequestMethod(httpRequestMethod)
                .logType(LogType.INFO)
                .message(message)
                .timestamp(LocalDateTime.now())
                .exception(null)
                .build();
    }
}
