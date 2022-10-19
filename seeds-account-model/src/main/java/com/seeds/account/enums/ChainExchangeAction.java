package com.seeds.account.enums;

import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;

import java.util.Arrays;
import java.util.Map;

/**
 * @author guocheng
 * @date 2021/1/6
 */
public enum ChainExchangeAction implements EnumWithCode {
    /**
     * 分红地址兑换
     */
    DIVIDEND_EXCHANGE(1),
    /**
     * 兑换地址兑换
     */
    SYSTEM_EXCHANGE(2),
    ;

    private Integer code;

    ChainExchangeAction(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public static final Map<Integer, ChainExchangeAction> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static ChainExchangeAction fromCode(int code) {
        return codeMap.get(code);
    }
}