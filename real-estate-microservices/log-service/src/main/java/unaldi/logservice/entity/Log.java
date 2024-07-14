package unaldi.logservice.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
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
@Document(collection = "logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    @Id
    private String id;

    @Field(name = "service_name")
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Field("http_request_method")
    private HttpRequestMethod httpRequestMethod;

    @Enumerated(EnumType.STRING)
    @Field("log_type")
    private LogType logType;

    @Field(name = "message")
    private String message;

    @Field(name = "timestamp")
    private LocalDateTime timestamp;

    @Field(name = "exception")
    private String exception;

}
