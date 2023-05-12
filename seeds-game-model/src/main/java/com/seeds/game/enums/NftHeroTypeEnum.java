package com.seeds.game.enums;
import lombok.Getter;


/**
 * NFT 英雄类型枚举
 */
@Getter
public enum NftHeroTypeEnum {
    Assassin(1, "Assassin2"),
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
}
