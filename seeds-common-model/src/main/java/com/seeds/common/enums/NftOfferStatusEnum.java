package com.seeds.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/9/5
 */
@Getter
public enum NftOfferStatusEnum {

    @JsonProperty("0")
    BIDDING((short) 0, "Offering","竞价中"),
    @JsonProperty("1")
    ACCEPTED((short) 1,"Accepted","已接受"),
    @JsonProperty("2")
    REJECTED((short) 2,"Rejected","已拒绝"),
    @JsonProperty("3")
    EXPIRED((short) 3,"Expired","已过期"),
    @JsonProperty("4")
    CANCELLED((short) 4,"Cancelled","已取消"),
    ;

    @JsonValue
    @EnumValue
    private Short code;
    private String descEn;
    private String desc;

    NftOfferStatusEnum(Short code, String descEn, String desc) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }
}