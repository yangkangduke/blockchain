package com.seeds.game.enums;
import lombok.Getter;

/**
 * nft 活动类型枚举
 *
 */
@Getter
public enum NftActivityEnum {

    MINT(0,"铸造"),
    LIST(1,"上架"),
    OFFERS(2,"拍卖"),
    SALE(3,"交易"),
    TRANSFER(4,"转移"),
    ;

    private int code;
    private String message;

    NftActivityEnum(int code, String message){
        this.code = code;
        this.message = message;
    }
}
