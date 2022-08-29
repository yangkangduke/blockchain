package com.seeds.admin.enums;

import com.seeds.common.exception.SeedsException;

/**
 * MetaMask状态
 *
 * @author yk
 * @date 2022/8/4
 */
public enum MetaMaskFlagEnum {

    DISABLE(0),
    ENABLED(1);

    private int value;

    MetaMaskFlagEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }


    public static MetaMaskFlagEnum from(int value) {
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
