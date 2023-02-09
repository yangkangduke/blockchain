package com.seeds.admin.enums;

import com.seeds.common.exception.SeedsException;

/**
 * 游戏状态枚举
 *
 * @author dengyang
 * @date 2023/2/9
 */
public enum GameStatusEnum {
    DISABLE(0),
    ENABLED(1),
    UNDER_MAINTENANCE(2);  //正在维护中

    private int value;

    GameStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static GameStatusEnum from(int value) {
        switch (value) {
            case 0:
                return DISABLE;
            case 1:
                return ENABLED;
            case 2:
                return UNDER_MAINTENANCE;
            default:
                throw new SeedsException("GameStatusEnum - no such enum for code: " + value);
        }
    }


}
