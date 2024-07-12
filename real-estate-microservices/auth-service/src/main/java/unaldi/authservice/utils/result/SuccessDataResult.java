package unaldi.authservice.utils.result;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
public class SuccessDataResult<T> extends DataResult<T> {

    public SuccessDataResult() {}

    public SuccessDataResult(T data) {
        super(true, data);
    }

    public SuccessDataResult(String message) {
        super(true, message, null);
    }

    public SuccessDataResult(T data, String message) {
        super(true, message, data);
    }

}
