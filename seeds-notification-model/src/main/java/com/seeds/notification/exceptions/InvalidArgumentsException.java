package com.seeds.notification.exceptions;

import com.seeds.notification.enums.NoticeErrorCodeEnum;
import lombok.Getter;

/**
* @author yk
 * @date 2020/8/1
 */
@Getter
public class InvalidArgumentsException extends GenericException {
    public InvalidArgumentsException(String message) {
        super(message);
    }

    public InvalidArgumentsException(NoticeErrorCodeEnum errorCode) {
        super(errorCode);
    }
}