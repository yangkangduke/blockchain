package com.seeds.account.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 账户操作
 *
 * @author yk
 */
@Getter
public enum AccountAction {
    /**
     * 充币
     */
    DEPOSIT(1, "account-deposit"),
    /**
     * 提币
     */
    WITHDRAW(2, "account-withdraw"),
    /**
     * 划转：钱包到交易
     */
    TRANSFER_WALLET_TO_TRADE(3),
    /**
     * 划转：交易到钱包
     */
    TRANSFER_TRADE_TO_WALLET(4),
    /**
     * 扣除
     */
    DEDUCT(5),
    /**
     * 退款
     */
    REFUND(6),
    /**
     * 转入 从其它人账户
     */
    TRANSFER_IN(7),
    /**
     * 转出 到其它人账户
     */
    TRANSFER_OUT(8),
    /**
     * 奖励
     */
    REWARD(9),
    /**
     * 返佣
     */
    REBATE(10),
    /**
     * 兑换 划入
     */
    EXCHANGE_IN(11),
    /**
     * 兑换 划出
     */
    EXCHANGE_OUT(12),

    /**
     * 资金费 收入
     */
    FUNDING_FEE_IN(13),

    /**
     * 资金费 扣除
     */
    FUNDING_FEE_OUT(14),

    /**
     * 爆仓
     */
    LIQUIDATION(15, "account-liquidation"),

    /**
     * 系统账户交易手续费
     */
    TRADING_FEE(16),

    /**
     * 冻结
     */
    FREEZE(17),

    /**
     * 解冻
     */
    UNFREEZE(18),

    /**
     * 账户类型变更
     */
    MARGIN_TYPE_CHANGE(19),

    /**
     * 杠杆变更
     */
    LEVERAGE_CHANGE(20),

    /**
     * 调整保证金
     */
    TRANSFER_MARGIN(21),

    /**
     * 交易
     */
    TRADE(22),

    /**
     * 高风险率告警
     */
    RISK_RATE_ALERT(23, "account-risk-rate-alert"),

    /**
     * 提币拒绝
     */
    WITHDRAW_REJECTED(24, "account-withdraw-rejected"),

    /**
     * 资金在母子账号之间归集
     */
    COLLECT(25),

    /**
     * 自动减仓
     */
    ADL(26, "account-adl"),

    /**
     * 链上兑换 划入
     */
    CHAIN_EXCHANGE_IN(27),

    /**
     * 链上兑换 划出
     */
    CHAIN_EXCHANGE_OUT(28),

    /**
     * 链上分红发放
     */
    CHAIN_DIVIDEND_TRANSFER(29),

    /**
     * 交易挖矿基础池奖励
     */
    TRADE_MINING_JUNIOR_REWARD(30),

    /**
     * 空投奖励
     */
    AIR_DROP(31),

    /**
     * 交易挖矿分红预算划拨
     */
    TRADE_MINING_BUDGET(32),

    /**
     * 交易挖矿进阶池奖励
     */
    TRADE_MINING_SENIOR_REWARD(33),

    /**
     * 锁定
     */
    LOCK(34),

    /**
     * 解锁
     */
    UNLOCK(35),

    /**
     * 其它
     */
    OTHERS(100);

    public static final Set<AccountAction> INVOLVING_ADL_CHANGE = new HashSet<>(Arrays.asList(
            TRADE, TRANSFER_WALLET_TO_TRADE, TRANSFER_TRADE_TO_WALLET, MARGIN_TYPE_CHANGE, LEVERAGE_CHANGE, TRANSFER_MARGIN
    ));

    @JsonValue
    @EnumValue
    Integer code;
    String notificationType;

    AccountAction(int code) {
        this.code = code;
    }

    AccountAction(int code, String notificationType) {
        this.code = code;
        this.notificationType = notificationType;
    }

    public static final Map<Integer, AccountAction> codeMap = Maps.newHashMap();

    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }

    public static AccountAction fromCode(int code) {
        return codeMap.get(code);
    }
}
