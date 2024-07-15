package unaldi.authservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import unaldi.authservice.entity.RefreshToken;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.response.*;
import unaldi.authservice.service.AuthService;
import unaldi.authservice.service.mapper.AuthMapper;
import unaldi.authservice.utils.constants.ExceptionMessages;
import unaldi.authservice.utils.constants.Messages;
import unaldi.authservice.utils.exception.RefreshTokenEmptyException;
import unaldi.authservice.utils.exception.RefreshTokenNotFoundException;
import unaldi.authservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.authservice.utils.rabbitMQ.enums.HttpRequestMethod;
import unaldi.authservice.utils.rabbitMQ.enums.LogType;
import unaldi.authservice.utils.rabbitMQ.producer.LogProducer;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final LogProducer logProducer;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtUtils jwtUtils, LogProducer logProducer) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
        this.logProducer = logProducer;
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

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        UserResponse userResponse = AuthMapper.INSTANCE.userDetailsToUserResponse(userDetails);
        SuccessDataResult<UserResponse> user = new SuccessDataResult<>(userResponse, Messages.USER_LOGGED_IN);

        logProducer.sendToLog(prepareLogDTO(Messages.USER_LOGGED_IN));

        return AuthMapper.INSTANCE.mapToLoginResponse(user, jwtCookie, jwtRefreshCookie);
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

        logProducer.sendToLog(prepareLogDTO(Messages.USER_LOGGED_OUT));

        return AuthMapper.INSTANCE.mapToLogoutResponse(result, jwtCookie, jwtRefreshCookie);
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

                        logProducer.sendToLog(prepareLogDTO(Messages.TOKEN_REFRESHED));

                        return AuthMapper.INSTANCE.mapToRefreshTokenResponse(result, jwtCookie);
                    })
                    .orElseThrow(() -> new RefreshTokenNotFoundException(refreshToken, ExceptionMessages.REFRESH_TOKEN_NOT_FOUND));
        }

        throw new RefreshTokenEmptyException(ExceptionMessages.REFRESH_TOKEN_EMPTY);
    }

    private LogDTO prepareLogDTO(String message) {
        return LogDTO
                .builder()
                .serviceName("auth-service")
                .httpRequestMethod(HttpRequestMethod.POST)
                .logType(LogType.INFO)
                .message(message)
                .timestamp(LocalDateTime.now())
                .exception(null)
                .build();
    }

}
