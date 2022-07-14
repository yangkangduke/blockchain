package com.seeds.uc.model.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/26
 */
@Getter
public enum ClientTypeEnum {
    @JsonProperty("0")
    INVALID((short) 0, "invalid"),
    @JsonProperty("1")
    NORMAL((short) 1, "normal"),
    ;

    @JsonValue
    @EnumValue
    private Short code;
    private String desc;

    ClientTypeEnum(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ClientTypeEnum from(Short code) {
        switch (code) {
            case 0:
                return INVALID;
            case 1:
                return NORMAL;
            default:
                throw new SeedsException("ClientTypeEnum - no such enum for code: " + code);
        }
    }

}
