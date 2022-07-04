package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCode;
import lombok.Getter;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
@Getter
public class InvalidArgumentsException extends GenericException {
    public InvalidArgumentsException(String message) {
        super(message);
    }

    public InvalidArgumentsException(UcErrorCode errorCode) {
        super(errorCode);
    }
}