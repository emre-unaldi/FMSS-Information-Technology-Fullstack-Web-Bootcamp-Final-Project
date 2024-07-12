package unaldi.authservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import unaldi.authservice.entity.RefreshToken;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.response.*;
import unaldi.authservice.service.AuthService;
import unaldi.authservice.utils.constants.ExceptionMessages;
import unaldi.authservice.utils.constants.Messages;
import unaldi.authservice.utils.exception.RefreshTokenEmptyException;
import unaldi.authservice.utils.exception.RefreshTokenException;
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
    public AuthenticationResponse login(LoginRequest loginRequest) {

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
    public LogoutResponse logout() {
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

}
