package com.seeds.admin.enums;

import lombok.Getter;

/**
 * @author hewei
 * @date 2022/4/16
 */
@Getter
public enum AutoIdApplyStateEnum {
    NO_APPLY(0, "NO_APPLY"),
    APPLYING(1, "APPLYING"),
    APPLIED(2, "APPLIED"),
    ;

    private final int code;
    private final String desc;

    AutoIdApplyStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
