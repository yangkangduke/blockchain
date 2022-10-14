package com.seeds.account.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 * 账户系统配置
 *
 * @author yk
 */
@Getter
public enum AccountSystemConfig {

    /**
     * 最大风险率
     */
    @Deprecated
    MAX_CROSSED_RISK_RATE(101, "account", "max_crossed_risk_rate"),

    /**
     * 风险率计算缓冲
     */
    @Deprecated
    MAX_CROSSED_RISK_RATE_BUFFER(102, "account", "max_crossed_risk_rate_buffer"),

    /**
     * 最大强平风险率
     */
    LIQUIDATION_RISK_RATE(103, "account", "liquidation_risk_rate"),

    /**
     * 有效价格时间
     */
    PRICE_VALID_TIME(104, "account", "price_valid_time"),

    /**
     * 内部提币是否返还手续费
     */
    INTERNAL_WITHDRAW_FEE_RETURN(105, "account", "internal_withdraw_fee_return"),

    /**
     * 放入观察者列表的风险率
     */
    OBSERVED_RISK_RATE(106, "account", "observed_risk_rate"),

    /**
     * 放入观察者列表的风险率
     */
    ALERT_RISK_RATE(107, "account", "alert_risk_rate"),

    /**
     * 用于检查爆仓的风险率
     */
    CHECK_LIQUIDATION_RISK_RATE(108, "account", "check_liquidation_risk_rate"),

    /**
     * 高风险率用户限制
     */
    REDIS_HIGH_RISK_RATE_LIMIT(109, "account", "redis_high_risk_rate_limit"),

    /**
     * 高风险率有效期
     */
    REDIS_HIGH_RISK_RATE_EXPIRE(110, "account", "redis_high_risk_rate_expire"),

    /**
     * 下单时允许的价格滑点
     */
    TRADE_PRICE_SLIPPAGE_LIMIT(112, "account", "trade_price_slippage_limit"),

    /**
     * 兑换询价有效期buffer（前后端buffer）
     */
    EXCHANGE_ORDER_BUFFER(113, "account", "exchange_order_buffer"),

    /**
     * 资金费率batch update account
     */
    ACCOUNT_SETTLE_BATCH(114, "account", "settle_batch"),

    /**
     * 账户风险率告警间隔
     */
    ACCOUNT_ALERT_RISK_RATE_PAUSE_INTERVAL(115, "account", "account_alert_risk_rate_pause_interval"),

    /**
     * 最少空闲地址数
     */
    CHAIN_MIN_IDLE_ADDRESS(101, "chain", "min_idle_address"),

    /**
     * 默认的gas price
     */
    CHAIN_GAS_PRICE(102, "chain", "gas_price"),

    /**
     * Gas Limit
     */
    CHAIN_GAS_LIMIT(103, "chain", "gas_limit"),

    /**
     * 确认块数
     */
    CHAIN_CONFIRM_BLOCKS(104, "chain", "confirm_blocks"),

    /**
     * 确认块数
     */
    CHAIN_WITHDRAW_BATCH(105, "chain", "withdraw_batch"),

    /**
     * MCD报价滑点
     */
    CHAIN_REPORTER_VOLATILITY(106, "chain", "reporter_volatility"),

    CHAIN_REPORTER_PERIOD(107, "chain", "reporter_period"),

    CHAIN_EXCHANGE_BATCH(108, "chain", "exchange_batch"),

    CHAIN_DIVIDEND_BATCH(109, "chain", "dividend_batch"),

    CHAIN_REPLACE_BATCH(110, "chain", "replace_batch"),

    CHAIN_TXN_EXPIRE_TIME(111, "chain", "txn_expire_time"),

    CHAIN_MCD_MONITOR_PERIOD(112, "chain", "mcd_monitor_period"),

    CHAIN_UNISWAP_EVENT_TOPIC(113, "chain", "uniswap_event_topic"),

    /**
     * 链上 kusd total supply 有效期 in sec
     */
    CHAIN_KUSD_TOTAL_EXPIRE(114, "chain", "kusd_total_expire"),

    /**
     * 链上 kusd total supply 预警期, 超出报警, in sec
     */
    CHAIN_KUSD_TOTAL_WARNING(115, "chain", "kusd_total_warning"),

    /**
     * 所有币种的划转限额
     */
    CHAIN_ALL_GAS_LIMIT(116, "chain", "all_gas_limit"),

    /**
     * KINE滑点
     */
    CHAIN_KINE_VOLATILITY(117, "chain", "kine_volatility"),

    CHAIN_KINE_PERIOD(118, "chain", "kine_period"),

    /**
     * MCD报价浮动上限
     */
    CHAIN_REPORTER_CEILING_VOLATILITY(119, "chain", "reporter_ceiling_volatility"),

    /**
     * MCD报价超过浮动上限时连续跳过上报次数
     */
    CHAIN_REPORTER_CEILING_TIMES(120, "chain", "reporter_ceiling_times"),

    /**
     * chain_web3j_rpc
     */
    CHAIN_RPC(121, "chain", "chain_web3j_rpc"),

    /**
     * 链上 mcd redis 缓存有效期 in sec
     */
    CHAIN_MCD_EXPIRE(122, "chain", "mcd_expire"),

    /**
     * 链上 mcd redis 缓存有效期 in sec
     */
    CHAIN_MCD_WARNING(123, "chain", "mcd_warning"),

    /**
     * 资金费率计算基点
     */
    ACCOUNT_FUNDING_RATE_BPS(124, "account", "funding_rate_bps"),

    /**
     * 链上发送交易间隔
     */
    CHAIN_TRANSACTION_SEND_SLEEP(125, "chain", "transaction_send_sleep"),

    /**
     * 读取地址余额是的分批地址请求数量
     */
    FUND_COLLECT_REQUEST_BATCH_ADDRESS_SIZE(126, "fund_collect", "balance_request_batch_address_size"),

    /**
     * 分批请求的间隔
     */
    FUND_COLLECT_REQUEST_BATCH_SLEEP(127, "fund_collect", "balance_request_batch_sleep"),

    /**
     * 查询充币的周期（天数）
     */
    FUND_COLLECT_DEPOSIT_ADDRESS_LOOK_BACK(128, "fund_collect", "balance_request_deposit_look_back"),

    /**
     * MGT查询余额时返回的数据限制
     */
    FUND_COLLECT_BALANCE_LIMIT(129, "fund_collect", "balance_request_limit"),

    /**
     * 交易挖矿kine 分红精度
     */
    MINING_REWARD_KINE_DECIMAL(130, "mining_reward", "mining_reward_kine_decimal"),

    /**
     * 当前gas price 缓存有效期(秒)
     */
    CHAIN_GAS_PRICE_EXPIRE(132, "chain", "gas_price_expire"),

    /**
     *
     */
    ACCOUNT_PLACE_ORDER_DELAY(133, "account", "place_order_delay"),

    /**
     * WOO滑点
     */
    CHAIN_WOO_VOLATILITY(134, "chain", "woo_volatility"),

    CHAIN_WOO_PERIOD(135, "chain", "woo_period"),

    CHAIN_DEFI_WITHDRAW_DEADLINE(136, "chain", "chain_defi_withdraw_deadline"),

    /**
     * 每条链的初始 mcd price
     */
    CHAIN_INIT_MCD_PRICE(137, "chain", "chain_init_mcd_price"),

    /**
     * 链上质押 ignore markets
     */
    CHAIN_IGNORE_MARKETS(138, "chain", "chain_ignore_markets"),

    /**
     * 条件单有效期(秒)
     */
    CONDITIONAL_PENDING_EXPIRATION_SEC(139, "trade", "conditional_pending_expiration_sec"),

    /**
     * 最大条件单个数(每个币种)
     */
    MAX_PENDING_CONDITIONAL_ORDER_NUM(140, "trade", "max_pending_conditional_order_num"),

    /**
     * 条件单触发检查间隔(ms)
     */
    CONDITIONAL_ORDER_CHECK_INTERVAL(142, "trade", "conditional_order_check_interval"),

    /**
     * 条件单触发检查价差
     */
    CONDITIONAL_ORDER_CHECK_PRICE_VAR(143, "trade", "conditional_order_check_price_var"),

    /**
     * 价格时效(ms)
     */
    PRICE_EXPIRE_MS(144, "trade", "price_expire_ms"),

    /**
     * 价格波动记录时间(ms)
     */
    PRICE_RECORD_MS(145, "trade", "price_record_ms"),

    /**
     * slp随机起始值
     */
    SLP_RANGE_MIN(146, "trade", "slp_range_min"),

    /**
     * slp随机终止值
     */
    SLP_RANGE_MAX(146, "trade", "slp_range_max"),

    /**
     * price var config
     */
    PRICE_VAR_CONFIG(147, "price", "price_var_config"),

    /**
     * transfer buffer
     */
    TRANSFER_BUFFER(148, "account", "transfer_buffer"),

    /**
     * 分仓最大持仓数量
     */
    MAX_SUB_POSITION_COUNT(149, "account", "max_sub_position_count"),
    ;

    int code;
    String type;
    String key;

    AccountSystemConfig(int code, String type, String key) {
        this.code = code;
        this.type = type;
        this.key = key;
    }

    public static final Map<Integer, AccountSystemConfig> codeMap = Maps.newHashMap();

    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }

    public static AccountSystemConfig fromCode(int code) {
        return codeMap.get(code);
    }
}
