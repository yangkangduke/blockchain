package com.seeds.notification.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author hewei
 */
@Getter
public enum NoticeTypeEnum {

    ACCOUNT_DEPOSIT(1, "充币"),
    ACCOUNT_WITHDRAW(2, "提币"),
    ACCOUNT_WITHDRAW_REJECTED(3, "提币被拒绝"),
    ACCOUNT_BALANCE_CHANGE(4, "余额变更"),
    ACCOUNT_AUDIT(5, "审核");

    @JsonValue
    private final int code;
    private final String desc;

    NoticeTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
