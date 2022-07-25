package com.seeds.admin.exceptions;

import com.seeds.admin.enums.AdminErrorCodeEnum;
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

    public InvalidArgumentsException(AdminErrorCodeEnum errorCode) {
        super(errorCode);
    }
}