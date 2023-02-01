package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;
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

    public InvalidArgumentsException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }

    public InvalidArgumentsException(UcErrorCodeEnum errorCode, String message) {
        super(errorCode, message);
    }
}