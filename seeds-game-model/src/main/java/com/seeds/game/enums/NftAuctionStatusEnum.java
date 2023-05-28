package com.seeds.game.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.seeds.common.exception.SeedsException;
import lombok.Getter;

/**
 * @author hang.yu
 * @date 2023/05/27
 */
@Getter
public enum NftAuctionStatusEnum {

    @JsonProperty("0")
    AUCTIONING(0, "On Auction","拍卖中"),
    @JsonProperty("1")
    FINISHED(1,"Finished","已结束"),
    @JsonProperty("2")
    CANCELLED(2,"Cancelled","已取消"),
    @JsonProperty("3")
    SETTLEMENT(3,"In Settlement","结算中"),
    ;

    @JsonValue
    @EnumValue
    private int code;
    private String descEn;
    private String desc;

    NftAuctionStatusEnum(int code, String descEn, String desc) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }

    public static NftAuctionStatusEnum from(int code) {
        switch (code) {
            case 0:
                return NftAuctionStatusEnum.AUCTIONING;
            case 1:
                return NftAuctionStatusEnum.FINISHED;
            case 2:
                return NftAuctionStatusEnum.CANCELLED;
            case 3:
                return NftAuctionStatusEnum.SETTLEMENT;
            default:
                throw new SeedsException("NftOfferStatusEnum - no such enum for code: " + code);
        }
    }

}