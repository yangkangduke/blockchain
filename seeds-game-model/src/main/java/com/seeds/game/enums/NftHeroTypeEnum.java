package com.seeds.game.enums;
import lombok.Getter;

import java.util.Arrays;


/**
 * NFT 英雄类型枚举
 */
@Getter
public enum NftHeroTypeEnum {
    ASSASSIN(1, "ASSASSIN"),
    TANK(2, "TANK"),
    ARCHER(3, "ARCHER"),
    WARRIOR(4, "WARRIOR"),
    SUPPORT(5, "SUPPORT");

    private final int code;
    private final String name;

    NftHeroTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static int getCode(String name) {
        return Arrays.stream(values())
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(a -> a.getCode())
                .orElse(null);
    }
}
