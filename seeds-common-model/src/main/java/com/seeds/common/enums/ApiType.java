package com.seeds.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * api类型
 * @author hang.yu
 * @since 2022-10-10
 */
@Getter
public enum ApiType {

    TRADE_NOTIFICATION(1, "交易通知"),
    NFT_NOTIFICATION(2, "NFT通知"),
    PLAYER_WIN_RANK(3, "玩家胜场排行"),
    PROFILE_INFO(4, "个人游戏概括信息"),
    NFT_PACKAGE_DISTRIBUTE(5, "公共背包-分发nft"),
    NFT_PACKAGE_TAKEBACK(6, "公共背包-收回nft"),
    NFT_TO_NFT_NOTIFY(7, "toNft通知"),
    DEPOSIT_CHECK(8, "deposit 校验"),
    APPLY_AUTOID(9, "申请autoID"),
    SEND_TOKEN_ADDRESS(10, "皮肤mint成功后推送tokenAddress"),
    ;

    @JsonValue
    private final int code;
    private final String desc;

    ApiType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
