package com.seeds.common.enums;

public interface WithDesc {
    int getCode();

    String getDesc();

    String getDescEn();

    default String toDescString() {
        return getDescEn() + " [" + getDesc() + "]";
    }
}
