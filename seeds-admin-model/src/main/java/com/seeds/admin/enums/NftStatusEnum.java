package com.seeds.admin.enums;

import lombok.Getter;

/**
 * @author: hewei
 * @date 2022/8/24
 */
@Getter
public enum NftStatusEnum {
    ON_SALE(1, "在售"),
    NOT_ON_SALE(0, "未在售");

    private final int code;
    private final String desc;

    NftStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
