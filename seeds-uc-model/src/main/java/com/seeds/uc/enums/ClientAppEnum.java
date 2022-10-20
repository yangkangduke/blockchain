package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
* @author yk
 * @date 2020/7/25
 */
@Getter
public enum ClientAppEnum {
    @JsonProperty("0")
    OTHER((short) 0, "other", "其他"),
    @JsonProperty("1")
    GLOBAL((short) 1, " ", "全球站"),
    ;

    @JsonValue
    @EnumValue
    private Short code;
    private String desc;
    private String descCn;

    ClientAppEnum(Short code, String desc, String descCn) {
        this.code = code;
        this.desc = desc;
        this.descCn = descCn;
    }

    public static ClientAppEnum from(Short code) {
        switch (code) {
            case 0:
                return OTHER;
            case 1:
                return GLOBAL;
            default:
                throw new SeedsException("ClientAppEnum - no such enum for code: " + code);
        }
    }
}
