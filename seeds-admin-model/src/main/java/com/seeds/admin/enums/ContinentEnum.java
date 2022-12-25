package com.seeds.admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: hewei
 * @date 2022/12/21
 */
@Getter
@AllArgsConstructor
public enum ContinentEnum {

    ASIA("AS"),

    EUROPE("EU"),

    NORTH_AMERICA("NA"),

    SOUTH_AMERICA("SA"),

    AFRICA("AF"),

    OCEANIA("OA"),

    ANTARCTICAN("AN");

    @EnumValue
    private final String name;
}
