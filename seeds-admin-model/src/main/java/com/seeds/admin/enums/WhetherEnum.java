package com.seeds.admin.enums;

/**
 * 是否枚举
 *
 * @author hang.yu
 * @since 2022/7/14
 */
public enum WhetherEnum {

    YES(1),
    NO(0);

    private int value;

    WhetherEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}