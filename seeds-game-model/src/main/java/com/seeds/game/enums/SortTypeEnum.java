package com.seeds.game.enums;

import lombok.Getter;

/**
 * @author hang.yu
 * @date 2023/5/11
 */
@Getter
public enum SortTypeEnum {

    ASC(0, "升序"),
    DESC(1, "降序"),
    ;

    private final int code;
    private final String desc;

    SortTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}