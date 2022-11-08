package com.seeds.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 随机码类型
 * @author hang.yu
 * @since 2022-11-08
 */
@Getter
public enum RandomCodeType {

    INVITE(1, "邀请码"),
    ;

    @JsonValue
    private final int code;
    private final String desc;

    RandomCodeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
