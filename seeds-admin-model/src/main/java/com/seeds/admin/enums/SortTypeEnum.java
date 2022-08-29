package com.seeds.admin.enums;

import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/8/25
 */
@Getter
public enum SortTypeEnum {

    NEWEST(1, "最新"),
    RANK(2, "评分"),
    PRICE(3, "价格"),
    ;

    private final int code;
    private final String desc;

    SortTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SortTypeEnum from(int code) {
        switch (code) {
            case 1:
                return NEWEST;
            case 2:
                return RANK;
            case 3:
                return PRICE;
            default:
                throw new SeedsException("SortTypeEnum - no such enum for code: " + code);
        }
    }
}