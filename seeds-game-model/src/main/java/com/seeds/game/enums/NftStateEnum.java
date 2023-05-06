package com.seeds.game.enums;
import lombok.Getter;

/**
 * nft 状态枚举
 *
 */
@Getter
public enum NftStateEnum {

    UNDEPOSITED(0,"未托管"),
    DEPOSITED(1,"托管中"),
    ON_SHELF(2,"售卖中"),
    ON_AUCTION(3,"拍卖中"),
    AUCTION_END(4,"拍卖结束"),
    BURNED(5,"已销毁"),
    LOCKED(6,"已锁定"),
    IN_SETTLEMENT(7,"结算中"),
    ;

    private int code;
    private String message;

    NftStateEnum(int code, String message){
        this.code = code;
        this.message = message;
    }
}
