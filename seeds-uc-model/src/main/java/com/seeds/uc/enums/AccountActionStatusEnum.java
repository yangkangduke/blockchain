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
public enum AccountActionStatusEnum {
    @JsonProperty("1")
    PROCESSING((short) 1, "processing","进行中"),
    @JsonProperty("2")
    SUCCESS((short) 2,"success","成功"),
    @JsonProperty("3")
    FAIL((short) 3,"fail","失败"),
    ;
    @JsonValue
    @EnumValue
    private Short code;
    private String descEn;
    private String desc;

    AccountActionStatusEnum(Short code, String descEn, String desc) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }
}