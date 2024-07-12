package unaldi.authservice.utils.result;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
public class ErrorDataResult<T> extends DataResult<T> {

    public ErrorDataResult(T data) {
        super(false, data);
    }

    public ErrorDataResult(String message) {
        super(false, message, null);
    }

    public ErrorDataResult(T data, String message) {
        super(false, message, data);
    }

}
