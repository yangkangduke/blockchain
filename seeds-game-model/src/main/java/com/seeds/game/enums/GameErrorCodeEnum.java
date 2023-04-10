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

    ERR_401_NOT_LOGGED_IN(401, "请先登录", "please login first"),
    ERR_500_SYSTEM_BUSY(500, "系统繁忙，请稍后再试...", "system busy, please try again later"),
    ERR_502_ILLEGAL_ARGUMENTS(502, "参数错误", "wrong arguments"),
    ERR_504_MISSING_ARGUMENTS(504, "缺少参数", "missing arguments"),
    ERR_505_MISSING_UCTOKEN(505, "缺少ucToken", "missing ucToken"),
    ERR_506_INVALID_UCTOKEN(506, "无效ucToken", "invalid ucToken"),
    ERR_507_NO_PERMISSION(507, "没有权限", "No permission"),
    // nft
    ERR_10001_NFT_ITEM_NOT_EXIST(10001, "NFT物品不存在", "NFT item does not exist"),
    ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER(10002, "NFT物品不属于当前用户", "NFT item does not belong to current user"),
    ERR_10003_NFT_ITEM_HAVE_BEEN_ASSIGNED(10003, "NFT物品已经被分配", "NFT item have been assigned"),
    ERR_10004_NFT_ITEM_NOT_ASSIGNED(10004, "NFT物品未分配", "NFT item not assigned"),
    ERR_10005_NFT_ITEM_ALREADY_BELONGS_TO_THE_ROLE(10005, "NFT物品已经属于该角色", "NFT item already belongs to the role"),
    ERR_10006_NFT_PROCESSING(10006, "操作中...请稍后再试", "In operation... Please try again later"),
    ERR_10007_NFT_ITEM_IS_ALREADY_ON_SALE(10007, "NFT已经在售卖中", "NFT item is already on sale"),
    ERR_10008_NFT_ITEM_IS_DEPOSIT(10008, "NFT托管中", "NFT item is deposit"),
    ERR_10009_NFT_ITEM_HAS_NOT_BEEN_GENERATED(10009, "NFT还未生成", "NFT item has not been generated"),
    ERR_10010_NFT_ITEM_HAS_BEEN_REMOVAL(10010, "NFT已经下架", "NFT item has been removed from the shelves"),
    ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST(10011, "该NFT拍卖不存在", "This NFT item auction not exist"),
    ERR_10012_NFT_ITEM_AUCTION_HAS_ENDED(10012, "该NFT拍卖已经结束", "This NFT item auction has ended"),
    ERR_10013_NFT_ITEM_ALREADY_HAS(10013, "已拥有该NFT", "Already have this NFT item"),
    ERR_10014_NFT_ITEM_OFFER_NOT_BELONG_TO_CURRENT_USER(10014, "NFT出价不属于当前用户", "Already have this NFT item"),
    ERR_10015_NFT_ITEM_HAS_NOT_BEEN_DEPOSITED(10015, "NFT物品未托管，请托管后再试", "NFT items are not deposited, please try again after deposit"),
    ERR_10016_NFT_ITEM_HAS_BEEN_RETRIEVED(10016, "NFT物品已经取回", "NFT items has been retrieved"),
    ERR_10017_NFT_ITEM_IN_SETTLEMENT(10017, "NFT物品结算中", "NFT items in settlement"),
    // role
    ERR_20001_ROLE_LEVE_IS_LESS_THAN_TEN(20001, "角色等级不满十级", "The role level is less than ten"),
    ERR_20002_ROLE_NOT_EXIST(20002, "角色不存在", "The role does not exist"),
    ERR_20003_ROLE_NOT_BELONGS_TO_CURRENT_USER(20003, "角色不属于当前用户，无法分配", "The role does not belongs to current user and Unable to allocate"),
    ERR_20004_SERVER_REGION_NOT_EXIST(20004, "区服不存在", "The sever region not exist"),

    // game
    ERR_30001_GAME_IS_UNDER_MAINTENANCE(30001, "游戏正在维护中,Web端请稍后再操作", "The game is under maintenance. Please try again later"),

    // user
    ERR_40001_FAILED_TO_GET_USER_INFORMATION(40001, "获取用户信息失败", "Failed to get user information"),
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
