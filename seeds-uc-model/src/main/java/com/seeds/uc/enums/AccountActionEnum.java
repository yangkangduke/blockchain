package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author yk
 * @date 2022/8/16
 */
@Getter
public enum AccountActionEnum {
    @JsonProperty("1")
    DEPOSIT((short) 1, "deposit","充币"),
    @JsonProperty("2")
    WITHDRAW((short) 2,"withdraw","提币"),
    @JsonProperty("3")
    BUY_NFT((short) 3,"buy","购买NFT"),
    @JsonProperty("4")
    GAS_FEE((short) 4,"gasFee","扣除手续费"),
    ;
    @JsonValue
    @EnumValue
    private Short code;
    private String descEn;
    private String desc;

    AccountActionEnum(Short code, String descEn, String desc) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }
}