package com.seeds.game.enums;
import lombok.Getter;

/**
 * nft 类型枚举
 *
 */
@Getter
public enum NftTypeEnum {

    skin(1,"装备"),
    equip(2,"皮肤");


    private int code;
    private String message;

    NftTypeEnum(int code,String message){
        this.code = code;
        this.message = message;
    }
}
