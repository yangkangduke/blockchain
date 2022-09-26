package com.seeds.notification.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Getter
public enum NoticeErrorCodeEnum {

    ERR_401_NOT_LOGGED_IN(401, "请先登录", "please login first"),
    ERR_500_SYSTEM_BUSY(500, "系统繁忙，请稍后再试...", "system busy, please try again later"),
    ERR_502_ILLEGAL_ARGUMENTS(502, "参数错误", "wrong arguments"),
    ERR_504_MISSING_ARGUMENTS(504, "缺少参数", "missing arguments"),

    ERR_1001_WEBSOCKET_AUTH_FAILED(1001, "websocket认证失败", "websocket authentication failed");

    @JsonValue
    @EnumValue
    private Integer code;
    private String desc;
    private String descEn;

    NoticeErrorCodeEnum(Integer code, String desc, String descEn) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }
}