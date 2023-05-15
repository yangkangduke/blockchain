package com.seeds.game.enums;

import lombok.Getter;

import java.util.Arrays;


/**
 * NFT 英雄类型枚举
 */
@Getter
public enum NftHeroTypeEnum {
    ASSASSIN(1, "ASSASSIN", "Riffa"),
    TANK(2, "TANK", "Gadean"),
    ARCHER(3, "ARCHER", "Grinia"),
    WARRIOR(4, "WARRIOR", "Ironhead"),
    SUPPORT(5, "SUPPORT", "Mogaine");

    private final int code;
    private final String name;
    private final String hero;

    NftHeroTypeEnum(int code, String name, String hero) {
        this.code = code;
        this.name = name;
        this.hero = hero;
    }

    public static int getCode(String name) {
        return Arrays.stream(values())
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(a -> a.getCode())
                .orElse(0);
    }

    public static String getProfession(String hero) {
        return Arrays.stream(values())
                .filter(i -> i.getName().equalsIgnoreCase(hero))
                .findFirst()
                .map(a -> a.getName())
                .orElse(null);
    }
}
