package com.seeds.common.web.exception;

import com.seeds.common.exception.SeedsException;

public class AuthException extends SeedsException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
