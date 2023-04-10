package com.seeds.game.enums;
import lombok.Getter;

/**
 * nft 订单类型枚举
 *
 */
@Getter
public enum NftOrderTypeEnum {

    BUY_NOW(1,"一口价"),
    ON_AUCTION(2,"拍卖"),
    ;

    private int code;
    private String message;

    NftOrderTypeEnum(int code, String message){
        this.code = code;
        this.message = message;
    }
}
