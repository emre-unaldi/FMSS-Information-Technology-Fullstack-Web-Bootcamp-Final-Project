package unaldi.authservice.utils.advice;

import unaldi.authservice.utils.advice.dto.ExceptionResponse;
import unaldi.authservice.utils.exception.RefreshTokenEmptyException;
import unaldi.authservice.utils.exception.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ExceptionResponse> handleTokenRefreshException(RefreshTokenException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(prepareExceptionResponse(ex, HttpStatus.FORBIDDEN, request));
    }

    @ExceptionHandler(value = RefreshTokenEmptyException.class)
    public ResponseEntity<ExceptionResponse> handleTokenRefreshException(RefreshTokenEmptyException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(prepareExceptionResponse(ex, HttpStatus.BAD_REQUEST, request));
    }

    private ExceptionResponse prepareExceptionResponse(Exception ex, HttpStatus status, WebRequest request) {
        return new ExceptionResponse(
                status.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }
}
