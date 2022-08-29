package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
public class SecurityItemException extends GenericException {

    public SecurityItemException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }
}
