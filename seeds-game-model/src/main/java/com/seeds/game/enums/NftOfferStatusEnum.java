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
public enum NftOfferStatusEnum {

    @JsonProperty("0")
    OFFERING(0, "Offering","竞价中"),
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

    NftOfferStatusEnum(int code, String descEn, String desc) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }

    public static NftOfferStatusEnum from(int code) {
        switch (code) {
            case 0:
                return NftOfferStatusEnum.OFFERING;
            case 1:
                return NftOfferStatusEnum.FINISHED;
            case 2:
                return NftOfferStatusEnum.CANCELLED;
            case 3:
                return NftOfferStatusEnum.SETTLEMENT;
            default:
                throw new SeedsException("NftOfferStatusEnum - no such enum for code: " + code);
        }
    }

}