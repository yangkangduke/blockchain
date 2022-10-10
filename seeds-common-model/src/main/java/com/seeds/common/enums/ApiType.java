package com.seeds.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * api类型
 * @author hang.yu
 * @since 2022-10-10
 */
@Getter
public enum ApiType {

    TRADE_NOTIFICATION(1, "交易通知"),
    NFT_NOTIFICATION(2, "NFT通知"),
    ;

    @JsonValue
    private final int code;
    private final String desc;

    ApiType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
