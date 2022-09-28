package com.seeds.account.ex;

/**
 *
 * @author yk
 *
 */
public class MissingElementException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public MissingElementException() {
        super();
    }

    public MissingElementException(String message) {
        super(message);
    }

    public MissingElementException(String message, Exception ex) {
        super(message, ex);
    }

}
