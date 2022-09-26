package com.seeds.notification.exceptions;

import com.seeds.notification.enums.NoticeErrorCodeEnum;
import lombok.Getter;

/**
 * @author hewei
 */
@Getter
public class GenericException extends RuntimeException {
    protected NoticeErrorCodeEnum errorCode;

    public GenericException(String message) {
        super(message);
    }

    public GenericException(NoticeErrorCodeEnum errorCode) {
        super(errorCode.getDescEn());
        this.errorCode = errorCode;
    }
}
