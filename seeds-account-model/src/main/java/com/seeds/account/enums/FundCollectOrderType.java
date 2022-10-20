package com.seeds.account.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 钱包归集订单类型
 *
 * @author yk
 *
 */
@Getter
public enum FundCollectOrderType {
    /**
     * 从用户地址转入系统钱包地址
     */
    FROM_USER_TO_SYSTEM(1),

    /**
     * 从系统钱包到用户地址
     */
    FROM_SYSTEM_TO_USER(2),

    /**
     * 从系统钱包到系统钱包
     */
    FROM_SYSTEM_TO_SYSTEM(3),
    ;

    private Integer code;

    FundCollectOrderType(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static final Map<Integer, FundCollectOrderType> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static FundCollectOrderType fromCode(int code) {
        return codeMap.get(code);
    }
}
