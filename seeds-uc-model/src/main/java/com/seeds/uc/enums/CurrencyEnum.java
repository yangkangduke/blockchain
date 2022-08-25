package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author yk
 * @date 2022/8/16
 */
@Getter
public enum  CurrencyEnum {
    @JsonProperty("USDC")
    USDC("usdc", "usdc"),

    ;
    @JsonValue
    @EnumValue
    private String code;
    private String desc;

    CurrencyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}