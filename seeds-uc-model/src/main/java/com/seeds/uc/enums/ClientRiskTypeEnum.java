package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
* @author yk
 * @date 2020/8/2
 */
@Getter
public enum ClientRiskTypeEnum {
    // 无风险
    @JsonProperty("0")
    NO_RISK((short) 0, "no risk"),
    // 高风险
    @JsonProperty("1")
    HIGH_RISK((short) 1, "high risk"),
    // 中风险
    @JsonProperty("2")
    MID_RISK((short) 2, "mid risk"),
    ;

    @JsonValue
    @EnumValue
    private Short code;
    private String desc;

    ClientRiskTypeEnum(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ClientRiskTypeEnum from(Short code) {
        switch (code) {
            case 0:
                return NO_RISK;
            case 1:
                return HIGH_RISK;
            case 2:
                return MID_RISK;
            default:
                throw new SeedsException("ClientRiskType - no such enum for code: " + code);
        }
    }
}