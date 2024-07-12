package unaldi.authservice.utils.result;

import lombok.Getter;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
@Getter
public class DataResult<T> extends Result {

    private T data;

    public DataResult() {}

    public DataResult(Boolean success, T data) {
        super(success);
        this.data = data;
    }

    public DataResult(Boolean success, String message, T data) {
        super(success, message);
        this.data = data;
    }

}
