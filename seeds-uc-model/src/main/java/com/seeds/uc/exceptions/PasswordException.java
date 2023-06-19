package com.seeds.uc.exceptions;


import com.seeds.uc.enums.UcErrorCodeEnum;

/**
* @author yk
 * @date 2020/8/24
 */
public class PasswordException extends GenericException {
    public PasswordException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }

    public PasswordException(UcErrorCodeEnum errorCode, String message) {
        super(errorCode, message);
    }
}
