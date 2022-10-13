package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/9/5
 */
@Getter
public enum NFTAuctionStatusEnum {

    @JsonProperty("0")
    NONE(0, "None auction","未拍卖"),
    @JsonProperty("1")
    FORWARD(1,"Forward auction","正向拍卖"),
    @JsonProperty("2")
    REVERSE(2,"Reverse auction","反向拍卖"),
    @JsonProperty("3")
    BOTH(3,"Forward and reverse auction","正向和反向拍卖"),
    ;

    @JsonValue
    @EnumValue
    private Integer code;
    private String descEn;
    private String desc;

    NFTAuctionStatusEnum(Integer code, String descEn, String desc) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }
}