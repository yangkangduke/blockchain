package com.seeds.common.exception;

/**
 * Seeds generic exception
 */
public class SeedsException extends RuntimeException {

    public SeedsException(String message) {
        super(message);
    }

    public SeedsException(String message, Throwable cause) {
        super(message, cause);
    }

}
