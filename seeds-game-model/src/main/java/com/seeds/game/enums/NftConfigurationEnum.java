package com.seeds.game.enums;

import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/8/18
 */
@Getter
public enum NftConfigurationEnum {
    UNASSIGNED(0, "未分配"),
    ASSIGNED(1, "已分配");

    private final int code;
    private final String desc;

    NftConfigurationEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
