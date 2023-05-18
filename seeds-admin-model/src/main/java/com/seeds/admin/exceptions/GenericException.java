package com.seeds.admin.exceptions;

import com.seeds.admin.enums.AdminErrorCodeEnum;
import lombok.Getter;

/**
* @author yk
 * @date 2020/7/25
 */
@Getter
public class GenericException extends RuntimeException {
    protected AdminErrorCodeEnum errorCode;

    public GenericException(String message) {
        super(message);
        this.errorCode = AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY;
    }

    public GenericException(AdminErrorCodeEnum errorCode) {
        super(errorCode.getDescEn());
        this.errorCode = errorCode;
    }

    public GenericException(AdminErrorCodeEnum errorCode, String message) {
        super(errorCode.getDesc() + message);
        this.errorCode = errorCode;
    }
}
