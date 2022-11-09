package com.seeds.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * 随机码类型
 * @author hang.yu
 * @since 2022-11-08
 */
@Getter
public enum RandomCodeType {

    INVITE(1, "邀请码", "inviteCode"),
    ;

    @JsonValue
    private final int code;
    private final String desc;
    private final String descEn;

    RandomCodeType(int code, String desc, String descEn) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }

    public static RandomCodeType from(int code) {
        switch (code) {
            case 1:
                return INVITE;
            default:
                throw new SeedsException("SortTypeEnum - no such enum for code: " + code);
        }
    }
}
