package com.seeds.account.ex;


import com.seeds.common.enums.ErrorCode;

/**
 *
 * @author yk
 *
 */
public class ConfigException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    ErrorCode errorCode;

    public ConfigException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDesc());
    }
    public ConfigException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
