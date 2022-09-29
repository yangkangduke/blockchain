package com.seeds.account.ex;

/**
 *
 * @author yk
 *
 */
public class MissingLockException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public MissingLockException() {
        super();
    }

    public MissingLockException(String message) {
        super(message);
    }

    public MissingLockException(String message, Exception ex) {
        super(message, ex);
    }

}
