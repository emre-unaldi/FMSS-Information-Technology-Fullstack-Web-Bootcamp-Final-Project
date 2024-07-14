package unaldi.logservice.entity.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unaldi.logservice.entity.enums.HttpRequestMethod;
import unaldi.logservice.entity.enums.LogType;

import java.time.LocalDateTime;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogSaveRequest {

    @NotBlank
    private String serviceName;

    @NotNull
    private HttpRequestMethod httpRequestMethod;

    @NotNull
    private LogType logType;

    @NotBlank
    private String message;

    @NotNull
    @FutureOrPresent
    private LocalDateTime timestamp;

    @NotBlank
    private String exception;

}
