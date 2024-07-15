package unaldi.logservice.utils.result;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
public class SuccessResult extends Result {

    public SuccessResult() {
        super(true);
    }
    public SuccessResult(String message) {
        super(true, message);
    }

}
