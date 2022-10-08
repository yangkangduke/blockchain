package com.seeds.common.redis.account;

import com.seeds.common.exception.SeedsException;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * @author guocheng
 * @date 2020/12/23
 */
@UtilityClass
public class RedisKeys {

    public static final String FUNDING_RATE_KEY_PREFIX = "funding:rate:";

    /**
     * 钱包(定期汇总)
     */
    public static final String SYSTEM_WALLET_ASSETS = "account:summary:wallet";

    /**
     * 普通用户钱包(定期汇总)
     */
    public static final String SYSTEM_USER_WALLET_ASSETS = "account:summary:user-wallet";

    /**
     * 系统用户钱包(定期汇总)
     */
    public static final String SYSTEM_SYS_WALLET_ASSETS = "account:summary:sys-wallet";

    /**
     * 交易账户系统全平台持仓(定期汇总)
     */
    public static final String SYSTEM_TRADING_POSITIONS = "account:summary:trading";

    /**
     * 账户的风险率排名 RSortedSet
     */
    public static final String ACCOUNT_RISK_RATE = "account:riskrate";

    /**
     * 全仓风险率告警
     */
    public static final String ACCOUNT_ALERT_RISK_RATE_CROSSED = "account:alert:riskrate:crossed";

    /**
     * 逐仓风险率告警
     */
    public static final String ACCOUNT_ALERT_RISK_RATE_ISOLATED = "account:alert:riskrate:isolated";

    /**
     * ADL指数排名 TreeSet<AdlScoreDto>
     */
    public static final String ADL_SCORE = "account:adlScore:";

    /**
     * 总的 MCD total and price
     */
    public static final String CHAIN_MCD_TOTAL = "account:chain:mcd";

    /**
     * Account Chain Delta kUSD
     */
    public static final String CHAIN_DELTA_KUSD = "account:chain:deltaKusd";

    /**
     * 分链 MCD total and price
     */
    public static final String CHAIN_MCD = "account:chain:mcd:%s";

    /**
     * chain gas price
     */
    public static final String CHAIN_GAS_PRICE = "account:chain:gas:price:%s";

    /**
     * 系统 MCD price 上报跳过次数
     */
    public static final String CHAIN_MCD_SKIP_TIMES = "account:chain:mcd:skip:times";

    /**
     * 链上总 kusd total supply
     */
    public static final String CHAIN_KUSD_TOTAL = "account:chain:kusd:total";

    /**
     * 链上分链 kusd total supply
     */
    public static final String CHAIN_KUSD = "account:chain:kusd:%s";

    /**
     * LP total staking value prefix
     */
    public static final String CHAIN_LP_STAKING_VALUE_PREFIX = "account:chain:lp:staking:value:%s";

    /**
     * total staking value
     */
    public static final String CHAIN_STAKING_VALUE_TOTAL = "account:chain:staking:value:total";


    public static final String USER_EXCHANGE_TRANSACTION_PREFIX = "user:exchange:transaction:";
    public static final String TRADING_RULE_PREFIX = "trading:rules:general:";
    public static final String EXCHANGE_RULE_PREFIX = "exchange:rules:general:";
    public static final String EXCHANGE_CHAIN_ALLOWANCE = "exchange:chain:allowance:";
    public static final String AFFILIATE_USER_RATE_PREFIX = "affiliate:user:rate:";
    public static final String ACCOUNT_ORDER_PREFIX = "trading:account:order:";
    public static final String DIVIDEND_RULE_PREFIX = "dividend:rule:%s:%s";
    public static final String PENDING_MSG = "msg:pending:";
    public static final String PENDING_MSG_KEY_LIST = "msg:pending:key:";
    public static final String PENDING_MSG_LOCK = "msg:pending:lock:";
    public static final String FUND_COLLECT_BALANCE_GET_STATUS = "fund:collect:balance:get:status:%s";
    public static final String FUND_COLLECT_BALANCE_VALUE = "fund:collect:balance:value:%s";
    public static final String TRADE_VOL_SUM_TODAY = "trade:vol:today:";
    public static final String TRADE_VOL_SUM = "trade:vol:";
    public static final String TRADE_VOL_USERS = "trade:vol:users:";
    public static final String TRADE_VOL_QUALIFIED_USER_COUNT = "trade:vol:users:qualified:";
    public static final String TRADE_VOL_CACHED_START_TIMESTAMP = "trade:vol:timestamp:start:";
    public static final String TRADE_VOL_CACHED_END_TIMESTAMP = "trade:vol:timestamp:end:";
    public static final String ACCOUNT_TRADE_MINING_CONFIG = "account:trade:mining:config";
    public static final String TRADE_CONDITIONAL_ORDER_PENDING = "trade:conditional:order:pending:";
    public static final String TRADE_CONDITIONAL_ORDER_COUNT = "trade:conditional:order:count:";

    public static final String ACCOUNT_SLP_HISTORY_START_ID = "account:slp:history:start:id";

    /**
     * 资金费结算锁
     */
    private static final String FUNDING_RATE_SETTLE_LOCK_KEY = FUNDING_RATE_KEY_PREFIX + "settle:lock:%s";
    private static final String FUNDING_RATE_GLOBAL_SETTLE_LOCK_KEY = FUNDING_RATE_KEY_PREFIX + "settle:lock:global";
    private static final String FUNDING_RATE_SUMMARY_KEY = FUNDING_RATE_KEY_PREFIX + ":summary:%s";
    /**
     * dashboard 数据
     */
    private static final String DASHBOARD_CHAIN_KEY = "dashboard:chain";
    private static final String DASHBOARD_EXCHANGE_KEY = "dashboard:exchange";

    private static final String SYSTEM_CONFIG = "system:config:";

    private static final String SYSTEM_WALLET_CHAIN_BALANCES = "account:system:wallet:chain:balances";

    public static String getAccountRiskRateKey(int shardingItem) {
        return ACCOUNT_RISK_RATE + ":" + shardingItem;
    }

    public static String getChainMcdKey(int chain) {
        return String.format(CHAIN_MCD, chain);
    }

    public static String getChainMcdTotalKey() {
        return CHAIN_MCD_TOTAL;
    }

    public static String getChainMcdSkipTimes() {
        return CHAIN_MCD_SKIP_TIMES;
    }

    public static String getChainGasPriceKey(int chain) {
        return String.format(CHAIN_GAS_PRICE, chain);
    }

    public static String getChainKusdTotalKey() {
        return CHAIN_KUSD_TOTAL;
    }

    public static String getChainKusdKey(int chain) {
        return String.format(CHAIN_KUSD, chain);
    }

    public static String getChainLpStakingValuePrefix(String token) {
        return String.format(CHAIN_LP_STAKING_VALUE_PREFIX, token);
    }

    public static String getChainStakingValueTotal() {
        return CHAIN_STAKING_VALUE_TOTAL;
    }

    public static String getFundingRateSettleLockKey(String symbol) {
        if (Objects.isNull(symbol)) {
            throw new SeedsException("getAccountFundingSettleKey failed, symbol cant be null");
        }
        return String.format(FUNDING_RATE_SETTLE_LOCK_KEY, symbol);
    }

    public static String getFundingRateGlobalSettleLockKey() {
        return FUNDING_RATE_GLOBAL_SETTLE_LOCK_KEY;
    }

    public static String getFundingRateSummaryKey(String symbol) {
        if (Objects.isNull(symbol)) {
            throw new SeedsException("getAssetFundingRateSummaryKey failed, symbol cant be null");
        }
        return String.format(FUNDING_RATE_SUMMARY_KEY, symbol);
    }

    public static String getSymbol(String baseCurrency, String quoteCurrency) {
        return baseCurrency.concat(quoteCurrency);
    }

    public static String getUserExchangeTransactionPrefix() {
        return USER_EXCHANGE_TRANSACTION_PREFIX;
    }

    public static String getOrderFromAccountKey(long userId, String symbol, long positionId, long time) {
        return ACCOUNT_ORDER_PREFIX + userId + ":" + symbol + ":" + positionId + ":" + time;
    }

    public static String getDashboardChainKey() {
        return DASHBOARD_CHAIN_KEY;
    }

    public static String getDashboardExchangeKey() {
        return DASHBOARD_EXCHANGE_KEY;
    }

    public static String getExchangeChainAllowance(String currency) {
        return EXCHANGE_CHAIN_ALLOWANCE.concat(currency);
    }

    public static String getDividendRuleKey(String currency, String typeName) {
        return String.format(DIVIDEND_RULE_PREFIX, currency, typeName);
    }

    public static String getAdlScoreKey(String symbol, String memId) {
        return ADL_SCORE.concat(symbol).concat(":").concat(memId);
    }

    public static String getAccountAlertRiskRateCrossedKey(long userId) {
        return ACCOUNT_ALERT_RISK_RATE_CROSSED + ":" + userId;
    }

    public static String getAccountAlertRiskRateIsolatedKey(String symbol, long userId) {
        return ACCOUNT_ALERT_RISK_RATE_ISOLATED + ":" + symbol + ":" + userId;
    }

    public static String getPendingMsgKeyListKey(String appName) {
        return PENDING_MSG_KEY_LIST + appName;
    }

    public static String getPendingMsgKey(String appName, String topic, String messageId) {
        return PENDING_MSG + appName + ":" + topic + ":" + messageId;
    }

    public static String getPendingMsgLock(String appName) {
        return PENDING_MSG_LOCK + appName;
    }

    /**
     * @param date   20210305
     * @param userId
     * @return
     */
    public static String getTradeVolSumKeyByDate(String date, long userId) {
        return TRADE_VOL_SUM + date + ":" + userId;
    }

    public static String getTradeUsersKey(String date) {
        return TRADE_VOL_USERS + date;
    }

    public static String getTotalTradeVolSumKey() {
        return TRADE_VOL_SUM_TODAY + "total";
    }

    /**
     * @param date 20210305
     * @return
     */
    public static String getTotalTradeVolSumKey(String date) {
        return TRADE_VOL_SUM + date + ":total";
    }

    public static String getUserTradeVolSumKey(long userID) {
        return TRADE_VOL_SUM_TODAY + userID;
    }

    public static String getUserTradeStartTimestampKey(String instanceId) {
        return TRADE_VOL_CACHED_START_TIMESTAMP + instanceId;
    }

    public static String getUserTradeEndTimestampKey(String instanceId) {
        return TRADE_VOL_CACHED_END_TIMESTAMP + instanceId;
    }

    public static String getAccountTradeMiningConfig() {
        return ACCOUNT_TRADE_MINING_CONFIG;
    }

    public static String getTradeVolQualifiedUserCount() {
        return TRADE_VOL_QUALIFIED_USER_COUNT + "today";
    }

    public static String getPendingConditionalOrderKey(String symbol, String condition) {
        return TRADE_CONDITIONAL_ORDER_PENDING + symbol + ":" + condition;
    }

    public static String getPendingConditionalOrderKey(long userId, long orderId) {
        return TRADE_CONDITIONAL_ORDER_PENDING + userId + ":" + orderId;
    }

    public static String getPendingConditionalOrderCountKey(long userId, String symbol, String direction) {
        return TRADE_CONDITIONAL_ORDER_COUNT + symbol + ":" + userId + ":" + direction;
    }

    public static String getPendingConditionalOrderCountKey(long userId, String symbol) {
        return TRADE_CONDITIONAL_ORDER_COUNT + symbol + ":" + userId;
    }

    public static String getSystemConfigKey(String type, String key) {
        return SYSTEM_CONFIG + type + ":" + key;
    }

    public static String getFundCollectBalanceValue(int chain) {
        return String.format(FUND_COLLECT_BALANCE_VALUE, chain);
    }

    public static String getFundCollectBalanceGetStatus(int chain) {
        return String.format(FUND_COLLECT_BALANCE_GET_STATUS, chain);
    }

    public static String getChainDeltaKusdKey() {
        return CHAIN_DELTA_KUSD;
    }

    public static String getSystemWalletChainBalancesKey() {
        return SYSTEM_WALLET_CHAIN_BALANCES;
    }
}
