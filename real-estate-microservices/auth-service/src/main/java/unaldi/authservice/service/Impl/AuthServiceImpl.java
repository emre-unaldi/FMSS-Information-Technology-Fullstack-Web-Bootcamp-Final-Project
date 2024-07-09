package unaldi.authservice.service.Impl;

import unaldi.authservice.entity.ERole;
import unaldi.authservice.entity.RefreshToken;
import unaldi.authservice.entity.Role;
import unaldi.authservice.entity.User;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.request.SignupRequest;
import unaldi.authservice.entity.dto.response.*;
import unaldi.authservice.repository.RoleRepository;
import unaldi.authservice.repository.UserRepository;
import unaldi.authservice.service.AuthService;
import unaldi.authservice.utils.constants.ExceptionMessages;
import unaldi.authservice.utils.constants.Messages;
import unaldi.authservice.utils.exception.RefreshTokenEmptyException;
import unaldi.authservice.utils.exception.RoleNotFoundException;
import unaldi.authservice.utils.exception.RefreshTokenException;
import unaldi.authservice.utils.exception.UserAlreadyExistsException;
import unaldi.authservice.utils.security.jwt.JwtUtils;
import unaldi.authservice.utils.security.services.RefreshTokenService;
import unaldi.authservice.utils.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@Service
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;

    UserRepository userRepository;

    RoleRepository roleRepository;

    PasswordEncoder encoder;

    JwtUtils jwtUtils;

    RefreshTokenService refreshTokenService;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder encoder,
            JwtUtils jwtUtils,
            RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public AuthenticationResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        UserInfoResponse userInfoResponse = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPassword(),
                userDetails.getPhoneNumber(),
                roles
        );

        return new AuthenticationResponse(
                userInfoResponse,
                jwtCookie,
                jwtRefreshCookie
        );
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistsException(ExceptionMessages.USERNAME_ALREADY_TAKEN);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException(ExceptionMessages.EMAIL_ALREADY_TAKEN);
        }

        User user = new User(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhoneNumber()
        );

        Set<String> strRoles = signUpRequest.getRole();
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

        user.setRoles(roles);
        userRepository.save(user);

        return new MessageResponse(Messages.USER_SIGN_UP);
    }

    @Override
    public LogoutResponse logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Objects.equals(principle.toString(), "anonymousUser")) {
            Long userId = ((UserDetailsImpl) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return new LogoutResponse(
                jwtCookie,
                jwtRefreshCookie,
                new MessageResponse(Messages.USER_SIGN_OUT)
        );
    }

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
                        MessageResponse messageResponse = new MessageResponse(Messages.TOKEN_REFRESHED);

                        return new RefreshTokenResponse(jwtCookie, messageResponse);
                    })
                    .orElseThrow(() -> new RefreshTokenException(refreshToken, ExceptionMessages.REFRESH_TOKEN_NOT_FOUND));
        }

        throw new RefreshTokenEmptyException(ExceptionMessages.REFRESH_TOKEN_EMPTY);
    }

    @Override
    public List<UserInfoResponse> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    List<String> rolesAsStrings = user.getRoles().stream()
                            .map(Role::getName)
                            .map(Object::toString)
                            .toList();

                    return new UserInfoResponse(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getPhoneNumber(),
                            rolesAsStrings
                    );
                })
                .toList();
    }

    @Override
    public UserInfoResponse findById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    List<String> rolesAsStrings = user.getRoles().stream()
                            .map(Role::getName)
                            .map(Object::toString)
                            .toList();

                    return new UserInfoResponse(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getPhoneNumber(),
                            rolesAsStrings
                    );
                })
                .orElse(null);
    }
}
