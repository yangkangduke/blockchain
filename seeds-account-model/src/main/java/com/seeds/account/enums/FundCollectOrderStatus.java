package com.seeds.account.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 钱包归集订单结果
 *
 * @author milo
 * @see CommonActionStatus
 */
@Getter
public enum FundCollectOrderStatus {
    /**
     * 已完成
     */
    COMPLETED(1),

    /**
     * 进行中
     */
    PROCESSING(3),
    ;

    private Integer code;

    FundCollectOrderStatus(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static final Map<Integer, FundCollectOrderStatus> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static FundCollectOrderStatus fromCode(int code) {
        return codeMap.get(code);
    }
}
