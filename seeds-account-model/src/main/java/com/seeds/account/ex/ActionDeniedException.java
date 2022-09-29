package com.seeds.account.ex;

/**
 *
 * @author yk
 *
 */
public class ActionDeniedException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public ActionDeniedException() {
        super();
    }

    public ActionDeniedException(String message) {
        super(message);
    }

    public ActionDeniedException(String message, Exception ex) {
        super(message, ex);
    }

}
