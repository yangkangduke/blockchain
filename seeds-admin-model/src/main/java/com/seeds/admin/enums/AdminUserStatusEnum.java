package com.seeds.admin.enums;

/**
 * 用户状态
 *
 * @author hang.yu
 * @date 2022/7/13
 */
public enum AdminUserStatusEnum {

    DISABLE(0),
    ENABLED(1);

    private int value;

    AdminUserStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
