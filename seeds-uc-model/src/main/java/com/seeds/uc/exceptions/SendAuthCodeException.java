package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/23
 */
public class SendAuthCodeException extends GenericException {
    public SendAuthCodeException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }

    public SendAuthCodeException(String message) {
        super(message);
    }
}
