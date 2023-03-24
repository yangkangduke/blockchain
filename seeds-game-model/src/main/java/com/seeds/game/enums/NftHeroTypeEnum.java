package com.seeds.game.enums;
import lombok.Getter;


/**
 * NFT 英雄类型枚举
 */
@Getter
public enum NftHeroTypeEnum {
    DESTIN(1,"DESTIN"),
    AILITH(2,"AILITH"),
    AILSA(3,"AILSA"),
    NELA(4,"NELA"),
    CATHAL(5,"CATHAL");


    private final int code;
    private final String name;

    NftHeroTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }
}
