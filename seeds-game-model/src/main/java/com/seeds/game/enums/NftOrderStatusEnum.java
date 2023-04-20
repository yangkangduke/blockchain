package com.seeds.game.enums;
import lombok.Getter;

/**
 * nft 订单状态枚举
 *
 */
@Getter
public enum NftOrderStatusEnum {

    PENDING(1,"挂单中"),
    COMPLETED(2,"已成交"),
    CANCELED(3,"已取消"),
    ;

    private Integer code;
    private String message;

    NftOrderStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
