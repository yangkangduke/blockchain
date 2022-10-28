package com.seeds.admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Action {

    ADD("add"),
    EDIT("edit"),
    DELETE("delete"),
    ADL("adl"),
    EXCHANGE("exchange"),
    TRANSFER("transfer"),
    APPROVE("approve"),
    REJECT("reject"),
    REPLACED("replaced"),
    REPLACE("replace"),
    WITHDRAW("withdraw"),
    REPORT_MCD("report_mcd"),
    RESTART("restart"),
    CLOSE_POSITION("close_position"),
    RESET_PWD("reset_pwd"),


    UNKNOWN("unknown")


    ;


    @EnumValue
    private final String name;
}
