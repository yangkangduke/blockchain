package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
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
public enum CaptchaType {
    @JsonProperty("0")
    HUOBI((short) 0, "huobi"),
    @JsonProperty("1")
    ALINGOOGLE((short) 1, "阿里滑块或Google reCAPTCHA"),
    ;

    @JsonValue
    @EnumValue
    private Short code;
    private String desc;

    CaptchaType(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CaptchaType from(Short code) {
        switch (code) {
            case 0:
                return HUOBI;
            case 1:
                return ALINGOOGLE;
            default:
                throw new SeedsException("CaptchaType - no such enum for code: " + code);
        }
    }
}
