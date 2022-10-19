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
public enum ChainTxnReplaceAppType implements EnumWithCode {

    /**
     * 提币
     */
    WITHDRAW(1),

    /**
     * 热钱包划转
     */
    HOT_WALLET_TRANSFER(2),

    /**
     * 资金归集
     */
    CASH_COLLECT(3),

    /**
     * GAS 划转
     */
    GAS_TRANSFER(4),

    /**
     * 链上 mcd 上报历史
     */
    MCD_HIS(5),

    /**
     * 链上系统兑换历史
     */
    EXCHANGE_SYSTEM_HIS(6),

    /**
     * 链上分红兑换历史
     */
    EXCHANGE_DIVIDEND_HIS(7),

    /**
     * 链上分红发放历史
     */
    DIVIDEND_HIS(8),
    ;

    public static final short MIN_CODE = 1;
    public static final short MAX_CODE = 8;

    private Integer code;

    ChainTxnReplaceAppType(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public static final Map<Integer, ChainTxnReplaceAppType> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static ChainTxnReplaceAppType fromCode(int code) {
        return codeMap.get(code);
    }
}
