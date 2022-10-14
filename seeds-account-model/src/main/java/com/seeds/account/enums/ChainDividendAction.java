package com.seeds.account.enums;

import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;

import java.util.Arrays;
import java.util.Map;

/**
 * @author guocheng
 * @date 2021/1/6
 */
public enum ChainDividendAction implements EnumWithCode {
    /**
     * 分红地址 -> MINTER合约地址
     */
    TO_MINTER(1),

    TO_KINE_RANCH(2),

    TO_FARMING(3),
    ;

    private Integer code;

    ChainDividendAction(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public static final Map<Integer, ChainDividendAction> codeMap = Maps.newHashMap();

    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }

    public static ChainDividendAction fromCode(int code) {
        return codeMap.get(code);
    }
}