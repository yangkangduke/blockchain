package com.seeds.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 请求来源
 * @author hang.yu
 * @since 2022-10-10
 */
@Getter
public enum RequestSource {

    UC(1, "uc平台"),
    GAME(2, "游戏"),
    ;

    @JsonValue
    private final int code;
    private final String desc;

    RequestSource(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
