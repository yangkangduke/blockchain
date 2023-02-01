package com.seeds.game.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author hewei
 * @date 2023/1/31
 */
@Getter
public enum GameErrorCodeEnum {

    // nft
    ERR_10001_NFT_ITEM_NOT_EXIST(10001, "NFT物品不存在", "NFT item does not exist"),
    ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER(10002, "NFT物品不属于当前用户", "NFT item does not belong to current user"),
    ERR_10003_NFT_ITEM_HAVE_BEEN_ASSIGNED(10003, "NFT物品已经被分配", "NFT item have been assigned"),
    ERR_10004_NFT_ITEM_CANNOT_TRANSFER(10004, "NFT物品未分配，无法转移", "NFT item not assigned and cannot transfer"),
    ERR_10005_NFT_ITEM_ALREADY_BELONGS_TO_THE_ROLE(10005, "NFT物品已经属于该角色", "NFT item already belongs to the role"),
    // role
    ERR_20001_ROLE_LEVE_IS_LESS_THAN_TEN(20001, "角色等级不满十级", "The role level is less than ten"),
    ERR_20001_ROLE_NOT_EXIST(20002, "角色不存在", "The role does not exist"),
    ;

    @JsonValue
    @EnumValue
    private Integer code;
    private String desc;
    private String descEn;

    GameErrorCodeEnum(Integer code, String desc, String descEn) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }
}
