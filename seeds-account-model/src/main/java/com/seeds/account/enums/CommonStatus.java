package com.seeds.account.enums;

import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 通用状态
 *
 * @author milo
 *
 */
public enum CommonStatus implements EnumWithCode {
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

    private Integer code;

    CommonStatus(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public static final Map<Integer, CommonStatus> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static CommonStatus fromCode(int code) {
        return codeMap.get(code);
    }
}
