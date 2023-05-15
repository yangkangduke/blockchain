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
    private final String profession;
    private final String hero;

    NftHeroTypeEnum(int code, String profession, String hero) {
        this.code = code;
        this.profession = profession;
        this.hero = hero;
    }

    public static String getProfessionByCode(int code) {
        return Arrays.stream(values())
                .filter(i -> i.getCode() == code)
                .findFirst()
                .map(a -> a.getProfession())
                .orElse(null);
    }

    public static int getCode(String profession) {
        return Arrays.stream(values())
                .filter(i -> i.getProfession().equalsIgnoreCase(profession))
                .findFirst()
                .map(a -> a.getCode())
                .orElse(0);
    }

    public static String getProfession(String hero) {
        return Arrays.stream(values())
                .filter(i -> i.getHero().equalsIgnoreCase(hero))
                .findFirst()
                .map(a -> a.getProfession())
                .orElse(null);
    }
}
