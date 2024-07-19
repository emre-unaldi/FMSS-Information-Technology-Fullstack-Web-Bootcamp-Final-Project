package unaldi.authservice.service.Impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import unaldi.authservice.entity.Token;
import unaldi.authservice.entity.dto.request.LoginRequest;
import unaldi.authservice.entity.dto.response.*;
import unaldi.authservice.repository.TokenRepository;
import unaldi.authservice.service.AuthService;
import unaldi.authservice.service.mapper.AuthMapper;
import unaldi.authservice.utils.constants.Messages;
import unaldi.authservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.authservice.utils.rabbitMQ.enums.HttpRequestMethod;
import unaldi.authservice.utils.rabbitMQ.enums.LogType;
import unaldi.authservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.authservice.utils.result.DataResult;
import unaldi.authservice.utils.result.Result;
import unaldi.authservice.utils.result.SuccessDataResult;
import unaldi.authservice.utils.result.SuccessResult;
import unaldi.authservice.utils.security.jwt.JwtUtils;
import unaldi.authservice.utils.security.services.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

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
    private final JwtUtils jwtUtils;
    private final LogProducer logProducer;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, LogProducer logProducer, TokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.logProducer = logProducer;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    @Override
    public DataResult<UserResponse> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessToken = jwtUtils.generateJwtToken(userDetails);

        Optional<Token> optionalToken = tokenRepository.findByUserId(userDetails.getId());
        System.out.println(optionalToken.isPresent());
        if (optionalToken.isPresent()) {
            tokenRepository.deleteByUserId(userDetails.getId());
        }

        Token token = AuthMapper.INSTANCE.mapToToken(userDetails.getId(), accessToken);
        tokenRepository.save(token);

        UserResponse userResponse = AuthMapper.INSTANCE.userDetailsToUserResponse(userDetails, accessToken);

        logProducer.sendToLog(prepareLogDTO(Messages.USER_LOGGED_IN));

        return new SuccessDataResult<>(userResponse, Messages.USER_LOGGED_IN);
    }

    @Transactional
    @Override
    public Result logout() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Objects.equals(principle.toString(), "anonymousUser")) {
            Long userId = ((UserDetailsImpl) principle).getId();
            tokenRepository.deleteByUserId(userId);
        }

        SecurityContextHolder.getContext().setAuthentication(null);

        SuccessResult result = new SuccessResult(Messages.USER_LOGGED_OUT);

        logProducer.sendToLog(prepareLogDTO(Messages.USER_LOGGED_OUT));

        return result;
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
