package unaldi.photoservice.utils.controllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import unaldi.photoservice.utils.constants.ExceptionMessages;
import unaldi.photoservice.utils.controllerAdvice.dto.ExceptionResponse;
import unaldi.photoservice.utils.exception.*;
import unaldi.photoservice.utils.result.DataResult;
import unaldi.photoservice.utils.result.ErrorDataResult;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Optional;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PhotoNameEmptyException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handlePhotoNameEmptyException(PhotoNameEmptyException exception, WebRequest request) {
        logger.error("PhotoNameEmptyException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.BAD_REQUEST, request),
                        ExceptionMessages.PHOTO_NAME_EMPTY)
                );
    }

    @ExceptionHandler(InvalidPhotoFormatException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleInvalidPhotoFormatException(InvalidPhotoFormatException exception, WebRequest request) {
        logger.error("InvalidPhotoFormatException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.BAD_REQUEST, request),
                        ExceptionMessages.INVALID_PHOTO_FORMAT)
                );
    }

    @ExceptionHandler(InvalidPathSequenceException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleInvalidPathSequenceException(InvalidPathSequenceException exception, WebRequest request) {
        logger.error("InvalidPathSequenceException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.BAD_REQUEST, request),
                        ExceptionMessages.INVALID_PHOTO_NAME_PATH_SEQUENCE)
                );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception, WebRequest request) {
        logger.error("MaxUploadSizeExceededException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.PAYLOAD_TOO_LARGE, request),
                        ExceptionMessages.PHOTO_SIZE_MAX_LIMIT)
                );
    }

    @ExceptionHandler(PhotoAlreadyExistsException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handlePhotoAlreadyExistsException(PhotoAlreadyExistsException exception, WebRequest request) {
        logger.error("PhotoAlreadyExistsException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.CONFLICT, request),
                        ExceptionMessages.PHOTO_NAME_ALREADY_EXISTS)
                );
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handlePhotoNotFoundException(PhotoNotFoundException exception, WebRequest request) {
        logger.error("PhotoNotFoundException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.NOT_FOUND, request),
                        ExceptionMessages.PHOTO_NOT_FOUND)
                );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<DataResult<ExceptionResponse>> handleAllException(IOException exception, WebRequest request) {
        logger.error("IOException occurred : {0}", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDataResult<>(
                        prepareExceptionResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, request),
                        ExceptionMessages.INTERNAL_SERVER_ERROR)
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

        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .httpStatus(httpStatus)
                .httpStatusCode(httpStatus.value())
                .httpMethod(httpMethod)
                .errorType(exception.getClass().getSimpleName())
                .requestPath(requestPath)
                .build();
    }
}
