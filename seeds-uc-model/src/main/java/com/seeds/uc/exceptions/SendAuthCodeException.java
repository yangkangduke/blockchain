package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCode;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/23
 */
public class SendAuthCodeException extends GenericException {
    public SendAuthCodeException(UcErrorCode errorCode) {
        super(errorCode);
    }

    public SendAuthCodeException(String message) {
        super(message);
    }
}
