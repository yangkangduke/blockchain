package com.seeds.game.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * NFT NFT稀有度枚举
 */
@Getter
public enum NftRarityEnum {
    NORMAL(1, "NORMAL"),
    RARE(2, "RARE"),
    EPIC(3, "EPIC");

    private int code;
    private String message;

    NftRarityEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过desc获取code
     *
     * @param desc desc
     * @return code
     */
    public static int codeOfDesc(String desc) {
        return Arrays.stream(values())
                .filter(i -> i.getMessage().equalsIgnoreCase(desc))
                .findFirst()
                .map(a -> a.getCode())
                .orElse(null);
    }
}
