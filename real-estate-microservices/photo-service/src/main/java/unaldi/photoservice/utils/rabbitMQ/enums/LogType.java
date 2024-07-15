package unaldi.photoservice.utils.rabbitMQ.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
@Getter
@AllArgsConstructor
public enum LogType {
    ERROR("ERROR"),
    WARN("WARN"),
    INFO("INFO");

    private final String message;

}
