package unaldi.userservice.utils.controllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import unaldi.userservice.utils.constants.ExceptionMessages;
import unaldi.userservice.utils.controllerAdvice.dto.ExceptionResponse;
import unaldi.userservice.utils.exception.*;
import unaldi.userservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.userservice.utils.rabbitMQ.enums.HttpRequestMethod;
import unaldi.userservice.utils.rabbitMQ.enums.LogType;
import unaldi.userservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.userservice.utils.result.DataResult;
import unaldi.userservice.utils.result.ErrorDataResult;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final LogProducer logProducer;

    @Autowired
    public GlobalExceptionHandler(LogProducer logProducer) {
        this.logProducer = logProducer;
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception, WebRequest request) {
        logger.error("UsernameAlreadyExistsException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.BAD_REQUEST, request),
                        ExceptionMessages.USERNAME_ALREADY_TAKEN)
                );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception, WebRequest request) {
        logger.error("EmailAlreadyExistsException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.BAD_REQUEST, request),
                        ExceptionMessages.EMAIL_ALREADY_TAKEN)
                );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleRoleNotFoundException(RoleNotFoundException exception, WebRequest request) {
        logger.error("RoleNotFoundException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.NOT_FOUND, request),
                        ExceptionMessages.ROLE_NOT_FOUND)
                );
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException exception, WebRequest request) {
        logger.error("RefreshTokenNotFoundException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.NOT_FOUND, request),
                        ExceptionMessages.REFRESH_TOKEN_NOT_FOUND)
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        logger.error("UserNotFoundException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.NOT_FOUND, request),
                        ExceptionMessages.USER_NOT_FOUND)
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleAllException(Exception exception, WebRequest request) {
        logger.error("Exception occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.BAD_REQUEST, request),
                        ExceptionMessages.BAD_REQUEST)
                );
    }

    private ExceptionResponse prepareExceptionResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        HttpServletRequest servletRequest = ((NativeWebRequest) request).getNativeRequest(HttpServletRequest.class);

        String httpMethod = Optional.ofNullable(servletRequest).map(HttpServletRequest::getMethod).orElse("Unknown");
        String requestPath = Optional.ofNullable(servletRequest).map(HttpServletRequest::getRequestURI).orElse("Unknown");
        String exceptionMessage = httpStatus + " - " + exception.getClass().getSimpleName();

        logProducer.sendToLog(prepareLogDTO(HttpRequestMethod.valueOf(httpMethod), exception.getMessage(), exceptionMessage));

        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(httpStatus)
                .httpStatusCode(httpStatus.value())
                .httpMethod(httpMethod)
                .errorType(exception.getClass().getSimpleName())
                .requestPath(requestPath)
                .build();
    }

    private LogDTO prepareLogDTO(HttpRequestMethod httpRequestMethod, String message, String exception) {
        return LogDTO
                .builder()
                .serviceName("user-service")
                .httpRequestMethod(httpRequestMethod)
                .logType(LogType.ERROR)
                .message(message)
                .timestamp(LocalDateTime.now())
                .exception(exception)
                .build();
    }

}
