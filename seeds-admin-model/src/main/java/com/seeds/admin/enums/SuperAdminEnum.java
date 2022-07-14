package com.seeds.admin.enums;

/**
 * 超级管理员枚举
 *
 * @author hang.yu
 * @since 2022/7/14
 */
public enum SuperAdminEnum {

    YES(1),
    NO(0);

    private int value;

    SuperAdminEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}