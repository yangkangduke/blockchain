package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;

/**
* @author yk
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
