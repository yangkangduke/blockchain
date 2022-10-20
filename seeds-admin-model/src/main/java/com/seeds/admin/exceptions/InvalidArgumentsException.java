package com.seeds.admin.exceptions;

import com.seeds.admin.enums.AdminErrorCodeEnum;
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

    public InvalidArgumentsException(AdminErrorCodeEnum errorCode) {
        super(errorCode);
    }
}