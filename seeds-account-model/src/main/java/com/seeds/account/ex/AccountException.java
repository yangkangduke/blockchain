package com.seeds.account.ex;


import com.seeds.common.enums.ErrorCode;

/**
 *
 * @author yk
 *
 */
public class AccountException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    ErrorCode errorCode;

    public AccountException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDesc());
    }
    public AccountException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
