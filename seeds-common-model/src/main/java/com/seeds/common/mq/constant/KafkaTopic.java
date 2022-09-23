package com.seeds.common.mq.constant;

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
    public final String NFT_DELETE_SUCCESS = "nft-delete-success";

    // Notification
    public final String SEND_NOTIFICATION = "send-notification";


}
