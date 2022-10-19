package com.seeds.account.enums;

import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 提币状态
 *
 * @author yk
 *
 */
public enum WithdrawStatus implements EnumWithCode {
    /**
     * 创建提币
     */
    CREATED(1),
    /**
     * 取消
     */
    CANCELLED(2),
    /**
     * 等待审核
     */
    PENDING_APPROVE(3),
    /**
     * 审核通过
     */
    APPROVED(4),
    /**
     * 审核拒绝
     */
    REJECTED(5),
    /**
     * 提交上链
     */
    TRANSACTION_ON_CHAIN(6),
    /**
     * 链上确认
     */
    TRANSACTION_CONFIRMED(7),
    /**
     * 链上失败
     */
    TRANSACTION_FAILED(8),

    /**
     * 交易取消
     */
    TRANSACTION_CANCELLED(9),

    /**
     * 链上失败，待处理
     */
    TRANSACTION_FAILED_PENDING_CHECK(10)
    ;

    private Integer code;

    WithdrawStatus(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public static final Map<Integer, WithdrawStatus> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static WithdrawStatus fromCode(int code) {
        return codeMap.get(code);
    }
}
