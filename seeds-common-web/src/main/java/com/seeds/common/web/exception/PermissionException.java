package com.seeds.common.web.exception;

import com.seeds.common.exception.SeedsException;

public class PermissionException extends SeedsException {

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

}
