package com.seeds.game.exception;

import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import lombok.Getter;

/**
 * @author yk
 * @date 2020/7/25
 */
@Getter
public class GenericException extends RuntimeException {
    protected GameErrorCodeEnum errorCode;

    public GenericException(String message) {
        super(message);
        this.errorCode = GameErrorCodeEnum.ERR_500_SYSTEM_BUSY;
    }

    public GenericException(GameErrorCodeEnum errorCode) {
        super(errorCode.getDescEn());
        this.errorCode = errorCode;
    }

    public GenericException(GameErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
