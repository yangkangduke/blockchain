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

    UNDER_MAINTENANCE(0,"游戏正在维护中"),
    NORMAL(1,"游戏正常");

    private int value;
    private  String condition;

    GameConditionEnum(int value,String condition) {
        this.value = value;
        this.condition = condition;
    }

    public int value() {
        return this.value;
    }

}
