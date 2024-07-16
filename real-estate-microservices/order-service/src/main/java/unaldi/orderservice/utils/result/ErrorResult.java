package unaldi.orderservice.utils.result;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
public class ErrorResult extends Result {

    public ErrorResult() {
        super(false);
    }

    public ErrorResult(String message) {
        super(false, message);
    }

}
