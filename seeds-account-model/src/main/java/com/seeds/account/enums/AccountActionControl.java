package com.seeds.account.enums;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * 账户操作
 *
 * @author yk
 *
 */
@Getter
public enum AccountActionControl {

    /**
     * 充币
     */
    CHAIN_DEPOSIT(1, "account", "chain_deposit"),

    /**
     * 提币
     */
    CHAIN_WITHDRAW(2, "account","chain_withdraw"),

    /**
     * 创建地址
     */
    CREATE_CHAIN_ADDRESS(3, "account","chain_create_address"),

    /**
     * 收取资金费率
     */
    FUNDING_RATE(4, "account","funding_rate"),

    /**
     * 爆仓
     */
    LIQUIDATION(5, "account","liquidation"),

    /**
     * 消息通知
     */
    PUBLISH_MESSAGE(6, "account", "publish_message"),

    /**
     * 下单
     */
    PLACE_ORDER(7, "account", "place_order"),

    /**
     * 划转
     */
    TRANSFER(8, "account", "transfer"),

    /**
     * 账户风险率计算
     */
    CALCULATE_RISK_RATE(9, "account", "calculate_risk_rate"),

    /**
     * 上报mcd
     */
    REPORT_MCD(10, "account", "report_mcd"),

    /**
     * 是否允许返佣划拨
     */
    AFFILIATE_MOVE(11, "account", "affiliate_move"),

    /**
     * 调整保证金
     */
    TRANSFER_MARGIN(12, "account", "transfer_margin"),

    /**
     * 运营dashboard
     */
    ACCOUNT_DASHBOARD_CALCULATION(13, "account", "dashboard_calculation"),

    /**
     * 更新kusd total supply redis 缓存
     */
    REFRESH_KUSD_TOTAL(14, "account", "refresh_kusd_total"),

    /**
     * 更新mcd redis 缓存
     */
    REFRESH_MCD_CACHE(15, "account", "refresh_mcd_cache"),

    /**
     * 是否允许交易挖矿分红发放
     */
    TRADE_MINING_REWARD(16, "account", "trade_mining_reward"),

    /**
     * 是否允许执行ADL
     */
    ADL(17, "account", "adl"),
    ;

    int code;
    String type;
    String key;

    AccountActionControl(int code, String type, String key) {
        this.code = code;
        this.type = type;
        this.key = key;
    }

    public static final Map<Integer, AccountActionControl> codeMap = Maps.newHashMap();
    static {
        Arrays.stream(values()).forEach(e -> codeMap.put(e.getCode(), e));
    }

    public static AccountActionControl fromCode(int code) {
        return codeMap.get(code);
    }
}
