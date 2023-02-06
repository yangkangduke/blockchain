package com.seeds.uc.exceptions;

import com.seeds.uc.enums.UcErrorCodeEnum;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
* @author yk
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

    public GenericException(UcErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
