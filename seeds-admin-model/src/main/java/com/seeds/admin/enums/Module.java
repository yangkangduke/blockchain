package com.seeds.admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum Module {

    USER_MANAGEMENT("use_management"),
    TRADE_MANAGEMENT("trade_management"),
    ASSET_MANAGEMENT("asset_management"),
    CASH_MANAGEMENT("cash_management"),
    RISK_MANAGEMENT("risk_management"),
    EXCHANGE_MANAGEMENT("exchange_management"),
    REPLACE_MANAGEMENT("replace_management"),
    API_KEY_MANAGEMENT("api_key_management"),
    BOURSE_MANAGEMENT("bourse_management"),
    AFFILIATE_MANAGEMENT("affiliate_management"),
    UNKNOWN("unknown")

    ;


    @EnumValue
    private final String name;

    public static Module getModule(String name) {
        for (Module o : values()) {
            if (Objects.equals(o.name, name)) {
                return o;
            }
        }
        return null;
    }
}
