package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
public class SecuritySettingException extends GenericException {
    public SecuritySettingException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }
}
