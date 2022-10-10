package com.seeds.account.enums;

import com.google.common.collect.Maps;
import com.seeds.common.enums.EnumWithCode;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 充币状态
 *
 * @author milo
 *
 */
public enum DepositStatus implements EnumWithCode {
    /**
     * 链上扫描到
     */
    CREATED(1),
    /**
     * 交易回滚
     */
    CANCELLED(2),
    /**
     * 等待人工审核（数额过大，黑地址）
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
     * 确认
     */
    TRANSACTION_CONFIRMED(7);

    private Integer code;
    DepositStatus(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public static final Map<Integer, DepositStatus> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }
    public static DepositStatus fromCode(int code) {
        return codeMap.get(code);
    }
}
