package com.seeds.account.enums;

import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 链操作
 *
 * @author milo
 *
 */
public enum ChainAction implements EnumWithCode {
    /**
     * 充币
     */
    DEPOSIT(1),
    /**
     * 提币
     */
    WITHDRAW(2);

    private Integer code;

    ChainAction(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public static final Map<Integer, ChainAction> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static ChainAction fromCode(int code) {
        return codeMap.get(code);
    }
}
