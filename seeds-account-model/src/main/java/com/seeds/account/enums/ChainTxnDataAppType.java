package com.seeds.account.enums;

import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 通用状态
 *
 * @author yk
 *
 */
public enum ChainTxnDataAppType implements EnumWithCode {
    /**
     * 链上兑换历史
     */
    EXCHANGE_HIS(1),

    /**
     * 链上分红划转历史
     */
    DIVIDEND_HIS(2),

    /**
     * 链上 mcd 上报历史
     */
    MCD_HIS(3),

    /**
     * 链上 replacement transaction
     */
    TXN_REPLACEMENT(4),

    /**
     * 提币
     */
    WITHDRAW(5),

    /**
     * 热钱包划转
     */
    HOT_WALLET_TRANSFER(6),

    /**
     * 资金归集
     */
    CASH_COLLECT(7),

    /**
     * GAS 划转
     */
    GAS_TRANSFER(8),
    ;

    private Integer code;

    ChainTxnDataAppType(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public static final Map<Integer, ChainTxnDataAppType> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static ChainTxnDataAppType fromCode(int code) {
        return codeMap.get(code);
    }
}
