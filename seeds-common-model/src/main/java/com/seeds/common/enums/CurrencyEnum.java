package com.seeds.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * @author yk
 * @date 2022/8/16
 */
@Getter
public enum  CurrencyEnum {
    @JsonProperty("USDC")
    USDC("usdc", "usdc"),
    @JsonProperty("USDT")
    USDT("usdt", "usdt"),

    ;
    @JsonValue
    @EnumValue
    private String code;
    private String desc;

    CurrencyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CurrencyEnum from(String code) {
        if (USDC.code.equalsIgnoreCase(code)) {
            return USDC;
        } else if (USDT.code.equalsIgnoreCase(code)) {
            return USDT;
        }
        throw new SeedsException("CurrencyEnum - no such enum for code: " + code);
    }

}