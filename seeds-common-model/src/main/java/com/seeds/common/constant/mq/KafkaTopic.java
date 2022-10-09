package com.seeds.common.constant.mq;

import lombok.experimental.UtilityClass;

/**
 * kafka topic常量定义
 *
 * @author: hewei
 * @date 2022/9/13
 */
@UtilityClass
public class KafkaTopic {

    // NFT
    public final String NFT_SAVE_SUCCESS = "nft-save-success";
    public final String GAME_NFT_SAVE_SUCCESS = "game-nft-save-success";
    public final String GAME_NFT_HONOR_MODIFY = "game-nft-honor-modify";
    public final String NFT_DELETE_SUCCESS = "nft-delete-success";

    // Notification
    public final String SEND_NOTIFICATION = "send-notification";


}
