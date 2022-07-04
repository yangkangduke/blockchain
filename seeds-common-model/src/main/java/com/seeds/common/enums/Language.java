package com.seeds.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/9/1
 */
@Getter
public enum Language {

    @JsonProperty("zh-cn")
    ZH_CN("zh-cn", "简体中文", "Simplified Chinese"),
    @JsonProperty("en-us")
    EN_US("en-us", "英语(美国)", "English"),
    ;

    @JsonValue
    private String code;
    private String desc;
    private String descCn;

    Language(String code, String desc, String descCn) {
        this.code = code;
        this.desc = desc;
        this.descCn = descCn;
    }

    public static final Language DEFAULT = ZH_CN;

    public static Language from(String code) {
        switch (code) {
            case "zh-cn":
                return ZH_CN;
            case "en-us":
                return EN_US;
            default:
                throw new SeedsException("Language - no such enum for code: " + code);
        }
    }
}
