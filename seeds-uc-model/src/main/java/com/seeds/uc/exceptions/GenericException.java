package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;
import lombok.Getter;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/25
 */
@Getter
public class GenericException extends RuntimeException {
    protected UcErrorCodeEnum errorCode;

    public GenericException(String message) {
        super(message);
    }

    public GenericException(UcErrorCodeEnum errorCode) {
        super(errorCode.getDescEn());
        this.errorCode = errorCode;
    }
}
