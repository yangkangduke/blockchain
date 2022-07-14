package com.seeds.admin.enums;

import com.seeds.common.exception.SeedsException;

/**
 * 用户状态
 *
 * @author hang.yu
 * @date 2022/7/13
 */
public enum SysUserStatusEnum {

    DISABLE(0),
    ENABLED(1);

    private int value;

    SysUserStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }


    public static SysUserStatusEnum from(int value) {
        switch (value) {
            case 0:
                return DISABLE;
            case 1:
                return ENABLED;
            default:
                throw new SeedsException("SysUserStatusEnum - no such enum for code: " + value);
        }
    }
}
