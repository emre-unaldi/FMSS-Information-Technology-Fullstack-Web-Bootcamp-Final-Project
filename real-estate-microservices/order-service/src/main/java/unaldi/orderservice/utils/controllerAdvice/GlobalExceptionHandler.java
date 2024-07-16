package unaldi.orderservice.utils.controllerAdvice;

import feign.FeignException;
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
import unaldi.orderservice.utils.constants.ExceptionMessages;
import unaldi.orderservice.utils.controllerAdvice.dto.ExceptionResponse;
import unaldi.orderservice.utils.exception.OrderNotFoundException;
import unaldi.orderservice.utils.exception.UserNotFoundException;
import unaldi.orderservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.orderservice.utils.rabbitMQ.enums.HttpRequestMethod;
import unaldi.orderservice.utils.rabbitMQ.enums.LogType;
import unaldi.orderservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.orderservice.utils.result.DataResult;
import unaldi.orderservice.utils.result.ErrorDataResult;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final LogProducer logProducer;

    @Autowired
    public GlobalExceptionHandler(LogProducer logProducer) {
        this.logProducer = logProducer;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleOrderNotFoundException(OrderNotFoundException exception, WebRequest request) {
        logger.error("OrderNotFoundException occurred : {}", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.NOT_FOUND, request),
                        ExceptionMessages.ORDER_NOT_FOUND)
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        logger.error("UserNotFoundException occurred : {}", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.NOT_FOUND, request),
                        ExceptionMessages.USER_NOT_FOUND)
                );
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleFeignNotFoundException(FeignException.NotFound exception, WebRequest request) {
        logger.error("Feign NotFoundException occurred : {}", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.NOT_FOUND, request),
                        ExceptionMessages.RESOURCE_NOT_FOUND)
                );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleAllFeignException(FeignException exception, WebRequest request) {
        logger.error("Feign exception occurred : {}", exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.BAD_REQUEST, request),
                        ExceptionMessages.FEIGN_BAD_REQUEST)
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleAllException(Exception exception, WebRequest request) {
        logger.error("Exception occurred : {}", exception);

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
                .serviceName("order-service")
                .httpRequestMethod(httpRequestMethod)
                .logType(LogType.ERROR)
                .message(message)
                .timestamp(LocalDateTime.now())
                .exception(exception)
                .build();
    }

}
