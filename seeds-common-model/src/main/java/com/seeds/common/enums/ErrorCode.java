package com.seeds.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sun.org.apache.bcel.internal.classfile.Code;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorCode implements WithDesc {

    UNAUTHORIZED(401, "未授权", "Unauthorized"),
    NOT_ACCESSIBLE(404, "不存在", "Not found"),

    UNKNOWN_ERROR(30000, "未知错误", "Unknown error"),
    ILLEGAL_PARAMS(30001, "非法参数", "Illegal parameters"),
    // 3xxxx 交易/账户 模块
    // 31xxx trade-service
    // 311xx 校验错误
    PRICE_TOLERANCE_EXCEEDED(31101, "价格变动", "Price change exceeded max tolerance"),
    MAX_OI_EXCEEDED(31102, "超过最大开仓量", "Max OI exceeded"),
    LESS_THAN_MIN_IO(31103, "不足最小开仓量", "Less than min OI"),
    INVALID_SYMBOL(31104, "无效交易对", "Invalid symbol"),
    SIZE_PRECISION_ERROR(31105, "数量精度错误", "Quantity precision error"),
    PRICE_PRECISION_ERROR(31106, "价格精度错误", "Price precision error"),
    NO_POS_FOR_LIQ(31107, "无可平仓的仓位", "No position for one-click liquidation"),
    INVALID_PARAMETER(31108, "参数错误", "Invalid parameter"),
    MAX_OI_PER_TRADE_EXCEEDED(31109, "超过单笔最大开仓量", "Max OI per trade exceeded"),
    MAX_OI_PER_USER_EXCEEDED(31110, "超过用户最大开仓量", "Max OI per user exceeded"),
    MAX_OI_BY_SYS_EXCEEDED(31111, "超过系统单币种总最大开仓量", "System max OI per symbol exceeded"),
    MAX_OI_BY_RISK_EXCEEDED(31112, "保证金不足", "Not enough margin"),
    SYMBOL_NOT_ENABLED(31113, "暂停交易", "Symbol not enabled"),
    NO_TOTAL_KUSD_DATA(31114, "无法获取KUSD total supply", "Cannot get kUSD total supply"),
    STORE_PENDING_FAILURE(31115, "存储条件单失败", "Failed to store pending condition order"),
    EXPIRED(31116, "条件单超时", "Condition order expired"),
    TOO_MANY_PENDING_ORDERS(31117, "条件单总数超过限制", "Too many condition orders"),
    ILLEGAL_TRIGGER_PRICE_TOO_CLOSE(31118, "触发价过于接近现价", "Trigger price too close to market price"),
    ILLEGAL_TRIGGER_PRICE_TOO_FAR(31119, "触发价过于偏离现价", "Trigger price too far from market price"),
    INVALID_PENDING_ORDER_ID(31120, "无效的待执行订单ID", "Invalid pending order ID"),
    ILLEGAL_EXISTING_POS(31121, "已有仓位与预期不符", "Illegal existing pos"),
    INVALID_TRIGGER_PRICE(31122, "触发条件失效", "Invalid trigger price"),
    ILLEGAL_TRIGGER_PRICE(31123, "非法触发价", "Illegal trigger price"),
    ILLEGAL_COUNTERPARTY(31124, "无效标识", "Illegal counterparty"),


    // 312xx 状态错误
    IN_SETTLEMENT(31201, "结算中", "In settlement"),
    // 313xx 系统异常
    PRICE_NOT_UPDATED(31301, "价格未更新", "Price not updated"),
    NO_TRADING_RULE(31303, "无法获取交易规则", "No trading rule"),
    // 314xx API 相关
    INVALID_TS(31401, "无效时间戳", "Invalid timestamp"),
    INVALID_AMOUNT(31402, "无效数量", "Invalid amount"),
    INVALID_DIRECTION(31403, "无效方向", "Invalid direction"),
    INVALID_CLIENT_ORDER_ID(31404, "非法自定义Order ID", "Illegal client order ID"),

    // 32xxx account-service
    // 321xx 校验错误
    ACCOUNT_VALIDATION_ERROR(32100, "validation error", "validation error"),
    // 322xx 状态错误
    ACCOUNT_BUSINESS_ERROR(32000, "business error", "business error"),
    POSITION_SYNC_ERROR(32201, "仓位变动", "Position changed during trade"),
    POSITION_EXCEED_MAX_COUNT(32202, "超出最大仓位数量", "Exceed max position count"),
    POSITION_UNKNOWN_ACTION(32203, "未知仓位操作", "Unknown position action"),
    POSITION_NOT_EXISTS(32204, "仓位不存在", "Unknown position"),

    // 323xx 系统异常
    ACCOUNT_UNKNOWN_ERROR(32300, "system error", "system error"),
    TRADE_ACCOUNT_NOT_FOUND(32301, "未找到交易账户", "Trade account not found"),
    ACCOUNT_CHANGE_ERROR(32302, "账户变动错误", "account change failed"),
    ACCOUNT_SYSTEM_CHANGE_ERROR(32303, "系统底仓变动错误", "system account change failed"),
    ACCOUNT_NO_RULE(32304, "无法获取规则", "no rule"),
    ACCOUNT_RULE_DISABLED(32305, "规则disabled", "rule disabled"),
    ACCOUNT_FAILED_ON_CHAIN(32306, "链上处理错误", "failed on chain"),

    ACCOUNT_IN_SETTLEMENT(32101, "in settlement"),
    ACCOUNT_TRANSFER_DISABLED(32102, "transfer disabled"),
    ACCOUNT_INVALID_TRANSFER_DIRECTION(32103, "invalid transfer direction"),
    ACCOUNT_INVALID_TRANSFER_AMOUNT(32104, "invalid transfer amount"),
    ACCOUNT_INVALID_TRANSFER_DECIMALS(32105, "invalid transfer amount decimals"),
    ACCOUNT_INSUFFICIENT_BALANCE(32106, "insufficient balance"),
    ACCOUNT_INVALID_SYMBOL(32107, "invalid symbol"),
    ACCOUNT_MUST_BE_ISOLATED(32108, "account must be isolated"),
    ACCOUNT_NO_POSITION(32109, "no position"),
    ACCOUNT_EXCEED_MAX_RISK_RATE(32110, "exceed max limit"),
    ACCOUNT_SWAP_MARGIN_TYPE_DISABLED(32112, "swap margin type disabled"),
    ACCOUNT_ALREADY_CROSSED(32113, "already crossed margin type"),
    ACCOUNT_ALREADY_ISOLATED(32114, "already isolated margin type"),
    ACCOUNT_INVALID_RISK_RATE(32115, "invalid leverage"),
    ACCOUNT_INVALID_LEVERAGE(32116, "invalid leverage"),
    ACCOUNT_HAVING_POSITION(32117, "having position"),
    ACCOUNT_WITHDRAW_DISABLED(32118, "withdraw disabled"),
    ACCOUNT_INVALID_WITHDRAW_CURRENCY(32119, "invalid withdraw currency"),
    ACCOUNT_INVALID_WITHDRAW_AMOUNT(32220, "invalid withdraw amount"),
    ACCOUNT_INVALID_WITHDRAW_FEE(32221, "invalid withdraw fee"),
    ACCOUNT_INVALID_WITHDRAW_ADDRESS(32222, "invalid withdraw address"),
    ACCOUNT_INVALID_WITHDRAW_TO_CONTRACT(32223, "withdraw address can not be an contract"),
    ACCOUNT_INVALID_WITHDRAW_TARGET(32224, "not supported to withdraw to yourself"),
    ACCOUNT_INVALID_WITHDRAW_BLACK_ADDRESS(32225, "withdraw address is disabled"),
    ACCOUNT_INVALID_WITHDRAW_AMOUNT_LESS_THAN_MIN(32226, "withdraw amount is less than min amount"),
    ACCOUNT_INVALID_WITHDRAW_AMOUNT_GREATER_THAN_MAX(32227, "withdraw amount is greater than max amount"),
    ACCOUNT_INVALID_WITHDRAW_EXCEED_INTRADAY_AMOUNT(32228, "withdraw exceed intraday amount"),
    ACCOUNT_INVALID_WITHDRAW_AUTHENTICATION(32229, "invalid withdraw authentication"),
    ACCOUNT_SYSTEM_INSUFFICIENT_BALANCE(32130, "system insufficient balance"),
    ACCOUNT_INVALID_EXCHANGE_USER(32131, "invalid exchange user"),
    ACCOUNT_INVALID_EXCHANGE_DECIMALS(32132, "invalid exchange decimals"),
    ACCOUNT_INVALID_EXCHANGE_AMOUNT(32133, "invalid exchange amount"),
    ACCOUNT_INVALID_EXCHANGE_INDIVIDUAL_AMOUNT(32134, "invalid exchange individual amount"),
    ACCOUNT_TRADE_IS_REQUIRED(32135, "trade is required"),
    ACCOUNT_CONFIG_UNKNOWN_PROP(32136, "unknown prop"),
    ACCOUNT_CONFIG_UNKNOWN_PROP_VALUE(32137, "unknown prop value"),
    ACCOUNT_INVALID_CHAIN(32138, "invalid chain"),
    ACCOUNT_INVALID_TAG(32139, "invalid tag"),
    ACCOUNT_INVALID_COMMENT(32140, "invalid comment"),
    ACCOUNT_INVALID_TRADING_MARGIN_CURRENCY(32141, "invalid trading margin currency"),
    ACCOUNT_CONTAIN_TRADING_ASSETS(32142, "contains trading assets"),
    //deposit config
    ILLEGAL_DEPOSIT_RULE_CONFIG(323143, "deposit configuration rule already exist"),
    //withdraw config
    ILLEGAL_WITHDRAW_RULE_CONFIG(323144, "withdraw configuration rule already exist"),

    //actionControl
    ILLEGAL_ACTION_CONTROL_CONFIG(323145,"There is already an enabled action control"),

    //blackList
    ILLEGAL_BLACK_LIST_CONFIG(323146,"There is already an enabled black list"),

    //withdrawWhiteList
    ILLEGAL_WITHDRAW_WHITE_LIST_CONFIG(232147,"There is already an enabled withdraw white list"),
    USER_ID_ON_CHAIN_ALREADY_EXIST(232148,"User id on chain already exist");
    @JsonValue
    private final int code;
    private final String desc;
    private final String descEn;

    ErrorCode(int code, String desc, String descEn) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }

    ErrorCode(int code, String desc) {
        this(code, desc, desc);
    }

    public static ErrorCode fromCode(int code) {
        return Arrays.stream(ErrorCode.values())
                .filter(v -> v.getCode() == code)
                .findFirst()
                .orElse(UNKNOWN_ERROR);
    }
}
