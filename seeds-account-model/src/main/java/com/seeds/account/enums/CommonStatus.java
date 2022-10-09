package com.seeds.account.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 通用状态
 *
 * @author milo
 *
 */
@Getter
public enum CommonStatus {
    /**
     * 启用
     */
    ENABLED(1),
    /**
     * 停用
     */
    DISABLED(2);

    public static final int minCode = 1;

    public static final int maxCode = 2;

    @EnumValue
    private Integer code;

    CommonStatus(int code) {
        this.code = code;
    }


    public static final Map<Integer, CommonStatus> codeMap = Maps.newHashMap();

    public static CommonStatus fromCode(int code) {
        return codeMap.get(code);
    }
}
