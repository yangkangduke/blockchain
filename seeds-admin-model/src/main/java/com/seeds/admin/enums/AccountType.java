package com.seeds.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountType {
    UNKNOWN(0, "未知账户"),
    SYSTEM_KINE_ACCOUNT(1, "系统总账户"),
    SYSTEM_TRADING_FEE(2, "交易手续费账户"),
    SYSTEM_WITHDRAW_FEE(3, "提币手续费账户"),
    SYSTEM_EXCHANGE(4, "兑换账户"),
    SYSTEM_DIVIDEND_CHAIN(5, "链上分红账户"),
    SYSTEM_OPERATION(6, "运营账户"),
    SYSTEM_TRADE(7, "总交易账户"),
    SYSTEM_REBATE(8, "返佣账户"),
    SYSTEM_LIQUIDATE(9, "爆仓归集账户"),
    SYSTEM_FUNDING_RATE(10, "资金费率归集账户"),
    SYSTEM_REWARD_ACCOUNT(11, "Kine分红系统账户"),
    SYSTEM_MCD_ADJUST(12, "MCD系统调节账户"),
    OTHERS(13, "其它"),
    SYSTEM_KINE_ACCOUNT_SUB(1001, "系统总账户(子账户)"),
    SYSTEM_TRADING_FEE_SUB(2001, "交易手续费账户(子账户)"),
    SYSTEM_WITHDRAW_FEE_SUB(3001, "提币手续费账户(子账户)"),
    SYSTEM_EXCHANGE_SUB(4001, "兑换账户(子账户)"),
    SYSTEM_DIVIDEND_CHAIN_SUB(5001, "链上分红账户(子账户)"),
    SYSTEM_OPERATION_SUB(6001, "运营账户(子账户)"),
    SYSTEM_TRADE_SUB(7001, "总交易账户(子账户)"),
    SYSTEM_REBATE_SUB(8001, "返佣账户(子账户)"),
    SYSTEM_LIQUIDATE_SUB(9001, "爆仓归集账户(子账户)"),
    SYSTEM_FUNDING_RATE_SUB(10001, "资金费率归集账户(子账户)"),
    SYSTEM_REWARD_ACCOUNT_SUB(11001, "Kine分红系统账户(子账户)"),
    SYSTEM_MCD_ADJUST_SUB(12001, "MCD系统调节账户(子账户)"),
    ;


    private final Integer code;
    private final String name;
}
