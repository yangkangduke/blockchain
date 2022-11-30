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
    public final String NFT_SAVE_SUCCESS = "NFT-SAVE-SUCCESS";
    public final String GAME_NFT_SAVE_SUCCESS = "GAME-NFT-SAVE-SUCCESS";
    public final String GAME_NFT_HONOR_MODIFY = "GAME-NFT-HONOR-MODIFY";
    public final String NFT_DELETE_SUCCESS = "NFT-DELETE-SUCCESS";
    public final String NFT_UPGRADE_SUCCESS = "NFT-UPGRADE-SUCCESS";

    // NFT ACCOUNT
    public final String AC_NFT_OWNER_CHANGE = "ac_nft_owner_change";

    // Notification
    public final String SEND_NOTIFICATION = "SEND-NOTIFICATION";

    //account
    public static final String TOPIC_ACCOUNT_UPDATE = "ACCOUNT-UPDATE";
    public static final String TOPIC_ACCOUNT_AUDIT = "ACCOUNT-UPDATE-AUDIT";

    // random code
    public final String RANDOM_CODE_GENERATE = "RANDOM_CODE_GENERATE";
    public final String RANDOM_CODE_EXPORT = "RANDOM_CODE_EXPORT";

}
