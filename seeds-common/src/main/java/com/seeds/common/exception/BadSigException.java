package com.seeds.common.exception;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/25
 */
public class BadSigException extends RuntimeException {
    public BadSigException(String message) {
        super(message);
    }
}
