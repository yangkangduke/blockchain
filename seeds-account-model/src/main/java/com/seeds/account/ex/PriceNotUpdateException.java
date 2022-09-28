package com.seeds.account.ex;

/**
 *
 * @author yk
 *
 */
public class PriceNotUpdateException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public PriceNotUpdateException() {
        super();
    }

    public PriceNotUpdateException(String message) {
        super(message);
    }

    public PriceNotUpdateException(String message, Exception ex) {
        super(message, ex);
    }

}
