package com.seeds.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: hewei
 * @date 2022/12/21
 */
@Getter
@AllArgsConstructor
public enum OsTypeEnum {

    WINDOWS(1, "Windows"),
    MAC(2, "Mac");

    private Integer code;
    private String name;

    public static String getNameByCode(Integer code) {
        for (OsTypeEnum value : OsTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
