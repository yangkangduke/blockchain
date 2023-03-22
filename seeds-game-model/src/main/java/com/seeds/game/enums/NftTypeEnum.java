package com.seeds.game.enums;
import lombok.Getter;

/**
 * nft 类型枚举
 *
 */
@Getter
public enum NftTypeEnum {

    skin(1,"皮肤"),
    equip(2,"装备");


    private int code;
    private String message;

    NftTypeEnum(int code,String message){
        this.code = code;
        this.message = message;
    }
}
