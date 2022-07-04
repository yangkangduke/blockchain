package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCode;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
public class SecuritySettingException extends GenericException {
    public SecuritySettingException(UcErrorCode errorCode) {
        super(errorCode);
    }
}
