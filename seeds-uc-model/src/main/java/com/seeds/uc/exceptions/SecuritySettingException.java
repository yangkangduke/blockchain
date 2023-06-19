package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;

/**
* @author yk
 * @date 2020/8/26
 */
public class SecuritySettingException extends GenericException {
    public SecuritySettingException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }

    public SecuritySettingException(UcErrorCodeEnum errorCode, String message) {
        super(errorCode, message);
    }
}
