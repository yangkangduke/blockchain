package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;
import lombok.Getter;

@Getter
public class LoginException extends GenericException {
    public LoginException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }

    public LoginException(UcErrorCodeEnum errorCode, String message) {
        super(errorCode, message);
    }
}
