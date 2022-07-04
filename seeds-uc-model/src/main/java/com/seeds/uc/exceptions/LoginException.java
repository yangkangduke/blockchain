package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCode;
import lombok.Getter;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
@Getter
public class LoginException extends GenericException {
    public LoginException(UcErrorCode errorCode) {
        super(errorCode);
    }
}
