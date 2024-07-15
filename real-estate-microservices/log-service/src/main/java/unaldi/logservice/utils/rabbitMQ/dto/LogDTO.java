package unaldi.logservice.utils.rabbitMQ.dto;

import lombok.*;
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
public class LogDTO {
    private String serviceName;
    private HttpRequestMethod httpRequestMethod;
    private LogType logType;
    private String message;
    private LocalDateTime timestamp;
    private String exception;
}
