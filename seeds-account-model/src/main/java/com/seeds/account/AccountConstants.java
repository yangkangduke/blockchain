package com.seeds.account;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yk
 */
public class AccountConstants {

    /**
     * 账户处理分片总数
     */
    public final static int ACCOUNT_SHARDING_TOTAL = 8;

    /**
     * 风险率精度 8
     */
    public final static int RISKRATE_DECIMALS = 4;

    /**
     * 划转精度 4
     */
    public final static int TRANSFER_DECIMALS = 4;

    /**
     * 杠杆精度 2
     */
    public final static int LEVERAGE_DECIMALS = 0;

    /**
     * 手续费率精度 6
     */
    public final static int FEE_RATE_DECIMALS = 6;

    /**
     * 手续费精度 8
     */
    public final static int FEE_DECIMALS = 8;

    /**
     * 充币精度
     */
    public final static int DEPOSIT_DECIMALS = 6;

    /**
     * 提币精度
     */
    public final static int WITHDRAW_DECIMALS = 6;

    /**
     * 资产精度
     */
    public final static int ASSET_DECIMALS = 4;

    /**
     * 盈亏精度
     */
    public final static int PROFIT_DECIMALS = 4;

    /**
     * 收益率精度
     */
    public final static int PROFIT_RATE_DECIMALS = 4;

    /**
     * 保证金率精度
     */
    public final static int MARGIN_RATIO_DECIMALS = 4;

    /**
     * 返佣精度
     */
    public final static int REBATE_DECIMALS = 4;

    /**
     * 兑换精度 in exchange rule table
     */
    /**
     * 计算中间值精度
     */
    public static final int CALC_DECIMAL_SCALE = 10;

    /**
     * 计算结果精度
     */
    public static final int CALC_FINAL_DECIMAL_SCALE = 8;

    /**
     * MCD price 计算精度
     */
    public static final int MCD_PRICE_DECIMAL_SCALE = 18;

    public final static BigDecimal HUNDRED = new BigDecimal("100");

    public final static BigDecimal HALF = new BigDecimal("0.5");

    public final static BigDecimal ONE_PERCENT = new BigDecimal("0.01");

    public final static String QUOTE_CURRENCY = "kUSD";

    public final static String KUSD_CURRENCY = "kUSD";

    public final static String ETHEREUM_CURRENCY = "ETH";

    public final static String USD = "USD";

    public final static String KINE = "KINE";

    public final static String WOO = "WOO";

    public final static String USDT = "USDT";

    public final static String USDC = "USDC";

    public final static String XKINE = "xKINE";

    public final static String MCD = "MCD";

    public final static List<String> IGNORE_ADDRESSES =
            Lists.newArrayList("0x0000000000000000000000000000000000000000",
                    "0xbd07a1d3625092aac2ebf97eade45bd4d35cceff");

    /**
     * 默认版本
     */
    public final static long DEFAULT_VERSION = 1L;

    /**
     * 未绑定用户
     */
    public final static long NOBODY_USER_ID = 0L;

    /**
     * 用户ID起始地址
     */
//    public final static long CLIENT_USER_ID = 1000_0000;
    public final static long CLIENT_USER_ID = 1;

    /**
     * 系统热点用户分片
     */
    public final static long USER_MOD = 32;

    public static long getClientUserId() {
        return CLIENT_USER_ID;
    }


    /**
     * 系统总账户（钱包账户）
     */
    private final static long SYSTEM_USER_ID = 1000;

    public static long getSystemUserId() {
        return SYSTEM_USER_ID;
    }

    /**
     * 系统交易手续费账户
     */
//    private final static long SYSTEM_TRADING_FEE_USER_ID = 1_0000;
//
//    public static long getSystemTradingFeeUserId() {
//        return SYSTEM_TRADING_FEE_USER_ID;
//    }
//
//    public static long getSystemTradingFeeUserId(long userId) {
//        return SYSTEM_TRADING_FEE_USER_ID + 1 + (userId % USER_MOD);
//    }
//
//    public static long getSystemTradingFeeStartUserId() {
//        return SYSTEM_TRADING_FEE_USER_ID + 1;
//    }
//
//    public static long getSystemTradingFeeEndUserId() {
//        return SYSTEM_TRADING_FEE_USER_ID + USER_MOD;
//    }

    /**
     * 系统提币手续费账户（钱包账户）
     */
    private final static long SYSTEM_WITHDRAW_FEE_USER_ID = 2_0000;

    public static long getSystemWithdrawFeeUserId() {
        return SYSTEM_WITHDRAW_FEE_USER_ID;
    }

//    /**
//     * 系统兑换账户（钱包账户）
//     */
    private final static long SYSTEM_EXCHANGE_USER_ID = 3_0000;

    public static long getSystemExchangeUserId() {
        return SYSTEM_EXCHANGE_USER_ID;
    }
//
//    /**
//     * 链上分红账户（钱包账户）
//     */
    private final static long SYSTEM_DIVIDEND_CHAIN_USER_ID = 4_0000;

    public static long getSystemDividendChainUserId() {
        return SYSTEM_DIVIDEND_CHAIN_USER_ID;
    }

    /**
     * 系统运营账户（钱包账户）
     */
    private final static long SYSTEM_OPERATION_USER_ID = 5_0000;

    public static long getSystemOperationUserId() {
        return SYSTEM_OPERATION_USER_ID;
    }

    /**
     * 系统总交易账户(交易账户)
     */
    private final static long SYSTEM_TRADE_USER_ID = 6_0000;

    public static long getSystemTradeUserId() {
        return SYSTEM_TRADE_USER_ID;
    }

    public static long getSystemTradeUserId(long userId) {
        return SYSTEM_TRADE_USER_ID + 1 + (userId % USER_MOD);
    }

    public static long getSystemTradeStartUserId() {
        return SYSTEM_TRADE_USER_ID + 1;
    }

    public static long getSystemTradeEndUserId() {
        return SYSTEM_TRADE_USER_ID + USER_MOD;
    }

    /**
     * 系统返佣账户（钱包账户）
     */
//    private final static long SYSTEM_REBATE_USER_ID = 7_0000;
//
//    public static long getSystemRebateUserId() {
//        return SYSTEM_REBATE_USER_ID;
//    }
//
//    public static long getSystemRebateUserId(long userId) {
//        return SYSTEM_REBATE_USER_ID + 1 + (userId % USER_MOD);
//    }
//
//    public static long getSystemRebateStartUserId() {
//        return SYSTEM_REBATE_USER_ID + 1;
//    }
//
//    public static long getSystemRebateEndUserId() {
//        return SYSTEM_REBATE_USER_ID + USER_MOD;
//    }


    /**
     * 系统返佣账户（钱包账户）
     */
//    private final static long SYSTEM_PROFIT_TAKEOFF_USER_ID = 8_0000;
//
//    public static long getSystemProfitTakeoffUserId() {
//        return SYSTEM_PROFIT_TAKEOFF_USER_ID;
//    }
//
//    public static long getSystemProfitTakeoffUserId(long userId) {
//        return SYSTEM_PROFIT_TAKEOFF_USER_ID + 1 + (userId % USER_MOD);
//    }
//
//    public static long getSystemProfitTakeoffStartUserId() {
//        return SYSTEM_PROFIT_TAKEOFF_USER_ID + 1;
//    }
//
//    public static long getSystemProfitTakeoffEndUserId() {
//        return SYSTEM_PROFIT_TAKEOFF_USER_ID + USER_MOD;
//    }

    /**
     * 爆仓归集账户（钱包账户）
     */
//    private final static long SYSTEM_LIQUIDATE_USER_ID = 9_0000;

//    public static long getSystemLiquidateUserId() {
//        return SYSTEM_LIQUIDATE_USER_ID;
//    }

//    /**
//     * 资金费率归集账户（钱包账户）
//     */
//    private final static long SYSTEM_FUNDING_RATE_USER_ID = 10_0000;
//
//    public static long getSystemFundingRateUserId() {
//        return SYSTEM_FUNDING_RATE_USER_ID;
//    }

//    /**
//     * Kine分红系统账户（钱包账户）
//     */
//    private final static long SYSTEM_KINE_REWARD_USER_ID = 11_0000;
//
//    public static long getSystemKineRewardUserId() {
//        return SYSTEM_KINE_REWARD_USER_ID;
//    }

    /**
     * MCD 系统调节账户
     */
//    private final static long SYSTEM_MCD_ADJUST_USER_ID = 12_0000;
//
//    public static long getSystemMcdAdjustUserId() {
//        return SYSTEM_MCD_ADJUST_USER_ID;
//    }

    private final static long USERID_RESERVE_INTERVAL = 1_0000;

    public static long getUseridReserveInterval() {
        return USERID_RESERVE_INTERVAL;
    }


    public static boolean isValidUserId(long userId) {
        return userId >= CLIENT_USER_ID || userId == SYSTEM_EXCHANGE_USER_ID || userId == SYSTEM_DIVIDEND_CHAIN_USER_ID;
    }

    public static boolean isValidUid(long userId) {
        return userId > 0 && userId <= 99999999;
    }

    /**
     * 代表全部交易对
     */
    public static final String SYMBOL_ALL = "all";

    /**
     * 滑点收入系统账户
     * @return
     */
    public static long getAccountSlpUserId() {
        return ACCOUNT_SLP_USERID;
    }
    public static final long ACCOUNT_SLP_USERID = 50001;
}
