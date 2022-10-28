package com.seeds.admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SubModule {

    WHITE_LIST_MANAGEMENT("white_list_management"),
    HOLD_POSITION_WHITE_LIST_MANAGEMENT("hold_position_white_list_management"),
    FEE_WHITE_LIST_MANAGEMENT("fee_white_list_management"),
    ASSET_BUSINESS_MANAGEMENT("asset_business_management"),
    ASSET_ALLOCATION("asset_allocation"),
    RISK_MANAGEMENT_SETTING("risk_management_setting"),
    MANUAL_ADL("manual_adl"),
    CLOSE_POSITION("close_position"),
    EXCHANGE_FORBIDDEN("exchange_forbidden"),
    WITHDRAW_FORBIDDEN("withdraw_forbidden"),
    WITHDRAW_BLACKLIST("withdraw_black_list"),
    DEPOSIT_FORBIDDEN("deposit_forbidden"),
    DEPOSIT_BLACKLIST("deposit_black_list"),
    ACTION_CONTROL("action_control"),
    SYSTEM_ACCOUNT("system_account_management"),
    WALLET_ACCOUNT("wallet_account_management"),
    DIVIDEND_MANAGEMENT("dividend_management"),
    MINING_MANAGEMENT("mining_management"),
    WITHDRAW_MANAGEMENT("withdraw_management"),
    WITHDRAW_CHAIN_CONFIG("withdraw_chain_config"),
    DEPOSIT_MANAGEMENT("deposit_management"),
    AFFILIATE_MANAGEMENT("affiliate_management"),
    EXCHANGE_SETTING("exchange_setting"),
    ALLOWANCE_SETTING("allowance_setting"),
    EXCHANGE_EXECUTION("exchange_execution"),
    EXCHANGE_BLACKLIST("exchange_blacklist"),
    TRADING_SETTING("trading_setting"),
    WITHDRAW_REVIEW("withdraw_review"),
    DEPOSIT_REVIEW("deposit_review"),
    ACCOUNT_TRANSFER("account_transfer"),
    GAS_FEE_TRANSFER("gas_fee_transfer"),
    CHAIN_REPLACE("chain_replace"),
    API_KEY("api_key"),
    PRICE_MANAGEMENT("price_management"),
    PARTNER_MANAGEMENT("partner_management"),
    APP_UPGRADE_MANAGEMENT("app_upgrade_management"),
    ADVERT_MANAGEMENT("advert_management"),
    NOTICE_MANAGEMENT("notice_management"),
    MARKET_ASSERT_MANAGEMENT("market_assert_management"),
    IMPORTANT_NOTICE_MANAGEMENT("important_notice_management"),
    USER_MANAGEMENT("user_management"),
    BLACKLIST_MANAGEMENT("blacklist_management"),
    AFFILIATE_SETTLEMENT("affiliate_settlement"),

    UNKNOWN("unknown")

    ;


    @EnumValue
    private final String name;

    public static SubModule getSubModule(String name) {
        for (SubModule o : values()) {
            if (Objects.equals(o.name, name)) {
                return o;
            }
        }
        return null;
    }
}
