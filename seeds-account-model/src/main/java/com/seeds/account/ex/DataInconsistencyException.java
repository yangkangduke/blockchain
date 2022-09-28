package com.seeds.account.ex;

/**
 *
 * @author yk
 *
 */
public class DataInconsistencyException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public DataInconsistencyException() {
        super();
    }

    public DataInconsistencyException(String message) {
        super(message);
    }

    public DataInconsistencyException(String message, Exception ex) {
        super(message, ex);
    }

}
