package com.seeds.uc.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/25
 */
@Getter
public enum ClientPlatformEnum {
    @JsonProperty("0")
    OTHER((short) 0, "other"),
    @JsonProperty("1")
    MAC((short) 1, "mac"),
    @JsonProperty("2")
    WINDOWS((short) 2, "windows"),
    @JsonProperty("3")
    IOS((short) 3, "ios"),
    @JsonProperty("4")
    ANDROID((short) 4, "android"),
    @JsonProperty("5")
    MOBILE((short) 5, "mobile"),
    ;

    @JsonValue
    private Short code;
    private String desc;

    ClientPlatformEnum(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ClientPlatformEnum from(Short code) {
        switch (code) {
            case 0:
                return OTHER;
            case 1:
                return MAC;
            case 2:
                return WINDOWS;
            case 3:
                return IOS;
            case 4:
                return ANDROID;
            case 5:
                return MOBILE;
            default:
                throw new SeedsException("ClientPlatformEnum - no such enum for code: " + code);
        }
    }


}
