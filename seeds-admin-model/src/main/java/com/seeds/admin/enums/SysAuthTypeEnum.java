package com.seeds.admin.enums;

import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Getter
public enum SysAuthTypeEnum {

    // 手机登录
    PHONE("1", "phone"),
    // 密码登录
    PASSWORD("2", "password"),
    ;

    private final String code;
    private final String desc;

    SysAuthTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SysAuthTypeEnum from(String code) {
        switch (code) {
            case "1":
                return PHONE;
            case "2":
                return PASSWORD;
            default:
                throw new SeedsException("AdminAuthTypeEnum - no such enum for code: " + code);
        }
    }

}