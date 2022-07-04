package com.seeds.uc.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
@Getter
public enum ClientRiskType {
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
    private Short code;
    private String desc;

    ClientRiskType(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ClientRiskType from(Short code) {
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
