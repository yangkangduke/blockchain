package com.seeds.game.enums;

import lombok.Getter;

/**
 * NFT NFT稀有度枚举
 */
@Getter
public enum NftRarityEnum {

    Common(1,"Common"),
    Rare(2,"Rare"),
    Epic(3,"Epic");

    private int code;
    private String message;

    NftRarityEnum(int code,String message){
        this.code = code;
        this.message = message;
    }
}
