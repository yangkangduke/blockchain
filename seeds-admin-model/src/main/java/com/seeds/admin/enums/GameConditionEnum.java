package com.seeds.admin.enums;

import lombok.Getter;

/**
 * 游戏状态枚举
 *
 * @author dengyang
 * @date 2023/2/9
 */
@Getter
public enum GameConditionEnum {

    UNDER_MAINTENANCE(0),  // 正在维护中
    NORMAL(1);             // 正常

    private int value;

    GameConditionEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
