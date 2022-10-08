package com.seeds.account.enums;

import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;

import java.util.Arrays;
import java.util.Map;

/**
 * 兑换状态
 */
public enum ExchangeStatus implements EnumWithCode {

    /**
     * 创建兑换
     */
    CREATED(1),
    /**
     * 成功
     */
    SUCCESS(2),
    /**
     * 失败
     */
    FAILED(3),
    ;

    private Integer code;

    ExchangeStatus(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public static final Map<Integer, ExchangeStatus> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static ExchangeStatus fromCode(int code) {
        return codeMap.get(code);
    }
}
