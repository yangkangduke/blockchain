package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * @author yk
 * @date 2022/7/25
 */
@Getter
public enum UserOperateEnum {
    @JsonProperty("1")
    REGISTER((short) 1, "register", "注册"),
    @JsonProperty("2")
    LOGIN((short) 2, "login", "登陆"),
    ;

    @JsonValue
    @EnumValue
    private Short code;
    private String desc;
    private String descCn;

    UserOperateEnum(Short code, String desc, String descCn) {
        this.code = code;
        this.desc = desc;
        this.descCn = descCn;
    }

    public static UserOperateEnum from(Short code) {
        switch (code) {
            case 1:
                return REGISTER;
            case 2:
                return LOGIN;
            default:
                throw new SeedsException("ClientAppEnum - no such enum for code: " + code);
        }
    }
}
