package unaldi.authservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import unaldi.authservice.entity.RefreshToken;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.response.*;
import unaldi.authservice.service.AuthService;
import unaldi.authservice.utils.constants.ExceptionMessages;
import unaldi.authservice.utils.constants.Messages;
import unaldi.authservice.utils.exception.RefreshTokenEmptyException;
import unaldi.authservice.utils.exception.RefreshTokenNotFoundException;
import unaldi.authservice.utils.result.SuccessDataResult;
import unaldi.authservice.utils.result.SuccessResult;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

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

        UserResponse userResponse = new UserResponse(
                userDetails.getId(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPassword(),
                userDetails.getPhoneNumber(),
                roles
        );

        SuccessDataResult<UserResponse> user = new SuccessDataResult<>(userResponse, Messages.USER_LOGGED_IN);

        return new LoginResponse(
                user,
                jwtCookie,
                jwtRefreshCookie
        );
    }

    @Override
    public LogoutResponse logout() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Objects.equals(principle.toString(), "anonymousUser")) {
            Long userId = ((UserDetailsImpl) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        SuccessResult result = new SuccessResult(Messages.USER_LOGGED_OUT);

        return new LogoutResponse(
                result,
                jwtCookie,
                jwtRefreshCookie
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
                        SuccessResult result = new SuccessResult(Messages.TOKEN_REFRESHED);
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return new RefreshTokenResponse(result, jwtCookie);
                    })
                    .orElseThrow(() -> new RefreshTokenNotFoundException(refreshToken, ExceptionMessages.REFRESH_TOKEN_NOT_FOUND));
        }

        throw new RefreshTokenEmptyException(ExceptionMessages.REFRESH_TOKEN_EMPTY);
    }

}
