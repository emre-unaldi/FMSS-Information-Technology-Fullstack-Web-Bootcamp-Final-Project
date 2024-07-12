package unaldi.authservice.utils.security.services;

import unaldi.authservice.entity.RefreshToken;
import unaldi.authservice.entity.User;
import unaldi.authservice.repository.RefreshTokenRepository;
import unaldi.authservice.repository.UserRepository;
import unaldi.authservice.utils.constants.ExceptionMessages;
import unaldi.authservice.utils.exception.RefreshTokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unaldi.authservice.utils.exception.UserNotFoundException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@Service
public class RefreshTokenService {

    @Value("${unaldi.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user.get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException(token.getToken(), ExceptionMessages.REFRESH_TOKEN_EXPIRED);
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }

        refreshTokenRepository.deleteByUser(user.get());
    }
}
