package unaldi.authservice.utils.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import unaldi.authservice.utils.constants.ExceptionMessages;
import unaldi.authservice.utils.controllerAdvice.dto.ExceptionResponse;
import unaldi.authservice.utils.result.ErrorDataResult;

import java.io.IOException;
import java.util.Optional;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 9.07.2024
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
    logger.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    ExceptionResponse exceptionResponse = prepareExceptionResponse(authException, request);
    ErrorDataResult<ExceptionResponse> result = new ErrorDataResult<>(exceptionResponse, ExceptionMessages.USER_UNAUTHORIZED);

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.writeValue(response.getOutputStream(), result);
  }

  private ExceptionResponse prepareExceptionResponse(Exception authException, HttpServletRequest servletRequest) {
    String httpMethod = Optional.ofNullable(servletRequest).map(HttpServletRequest::getMethod).orElse("Unknown");
    String requestPath = Optional.ofNullable(servletRequest).map(HttpServletRequest::getRequestURI).orElse("Unknown");

    return ExceptionResponse.builder()
            .message(authException.getMessage())
            .httpStatus(HttpStatus.UNAUTHORIZED)
            .httpStatusCode(HttpStatus.UNAUTHORIZED.value())
            .httpMethod(httpMethod)
            .errorType(authException.getClass().getSimpleName())
            .requestPath(requestPath)
            .build();
  }

}
