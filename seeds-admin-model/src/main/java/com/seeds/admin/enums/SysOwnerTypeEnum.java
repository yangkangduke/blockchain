package com.seeds.admin.enums;

import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/8/24
 */
@Getter
public enum SysOwnerTypeEnum {

    // 手机登录
    PLATFORM(0, "平台"),
    // 密码登录
    UC_USER(1, "uc用户"),
    ;

    private final int code;
    private final String desc;

    SysOwnerTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}