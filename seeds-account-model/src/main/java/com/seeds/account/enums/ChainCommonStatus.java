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
public enum ChainCommonStatus implements EnumWithCode {
    /**
     * 提交上链
     */
    TRANSACTION_ON_CHAIN(1),

    /**
     * 链上执行完毕，待安全确认（e.g. 12个块安全确认）
     */
    TRANSACTION_PENDING_CONFIRMED(2),
    /**
     * 链上确认
     */
    TRANSACTION_CONFIRMED(3),
    /**
     * 链上失败
     */
    TRANSACTION_FAILED(4),

    /**
     * 链上取消
     */
    TRANSACTION_CANCELLED(5),

    /**
     * 原tx被取消，replace tx 替代成功
     */
    TRANSACTION_CANCELLED_AND_REPLACED(6),
    ;

    private final Integer code;

    ChainCommonStatus(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public static final Map<Integer, ChainCommonStatus> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static ChainCommonStatus fromCode(Integer code) {
        return code == null ? null : codeMap.get(code);
    }
}
