package unaldi.orderservice.utils.rabbitMQ.dto;

import lombok.*;
import unaldi.orderservice.utils.rabbitMQ.enums.HttpRequestMethod;
import unaldi.orderservice.utils.rabbitMQ.enums.LogType;

import java.time.LocalDateTime;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LogDTO {
    private String serviceName;
    private HttpRequestMethod httpRequestMethod;
    private LogType logType;
    private String message;
    private LocalDateTime timestamp;
    private String exception;
}