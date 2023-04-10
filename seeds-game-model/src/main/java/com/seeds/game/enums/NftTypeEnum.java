package com.seeds.game.enums;
import lombok.Getter;

/**
 * nft 类型枚举
 *
 */
@Getter
public enum NftTypeEnum {

    equip(1,"装备"),
    props(2,"道具"),
    hero(3,"英雄"),
    skin(4,"皮肤");

    private int code;
    private String message;

    NftTypeEnum(int code,String message){
        this.code = code;
        this.message = message;
    }
}
