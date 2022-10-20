package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;
import lombok.Getter;

/**
* @author yk
 * @date 2020/8/1
 */
@Getter
public class RegistrationException extends GenericException {
    public RegistrationException(UcErrorCodeEnum errorCode) {
        super(errorCode);
    }
}