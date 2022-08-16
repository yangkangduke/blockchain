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
public enum AccountTypeEnum {
    @JsonProperty("1")
    ACTUALS(1, "actuals","现货"),

    ;
    @JsonValue
    @EnumValue
    private Integer code;
    private String descEn;
    private String desc;

    AccountTypeEnum(Integer code, String descEn, String desc) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }

}