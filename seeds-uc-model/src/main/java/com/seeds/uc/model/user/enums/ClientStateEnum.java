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
public enum ClientStateEnum {
    //  无效状态
    @JsonProperty("0")
    INVALID((short) 0, "invalid"),
    @JsonProperty("1")
    NORMAL((short) 1, "normal"),
    @JsonProperty("2")
    FROZEN((short) 2, "frozen"),
    @JsonProperty("3")
    CANCELLED((short) 3, "cancelled"),
    ;
    @JsonValue
    @EnumValue
    private Short code;
    private String desc;

    ClientStateEnum(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ClientStateEnum from(Short code) {
        switch (code) {
            case 0:
                return INVALID;
            case 1:
                return NORMAL;
            case 2:
                return FROZEN;
            case 3:
                return CANCELLED;
            default:
                throw new SeedsException("ClientStateEnum - no such enum for code: " + code);
        }
    }
}