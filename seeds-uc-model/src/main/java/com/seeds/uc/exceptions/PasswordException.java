package com.seeds.uc.exceptions;


import com.seeds.uc.enums.UcErrorCodeEnum;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/24
 */
public class PasswordException extends GenericException {
    public PasswordException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }
}
