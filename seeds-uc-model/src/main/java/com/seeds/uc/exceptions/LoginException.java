package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;
import lombok.Getter;

@Getter
public class LoginException extends GenericException {
    public LoginException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }
}
