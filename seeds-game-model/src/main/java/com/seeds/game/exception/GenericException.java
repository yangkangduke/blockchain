package com.seeds.game.exception;

import com.seeds.game.enums.GameErrorCodeEnum;
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
    }

    public GenericException(GameErrorCodeEnum errorCode) {
        super(errorCode.getDescEn());
        this.errorCode = errorCode;
    }
}
