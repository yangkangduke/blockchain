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
    // NFT PIC
    public final String NFT_PIC_ATTR_UPDATE_SUCCESS = "NFT_PIC_ATTR_UPDATE_SUCCESS";

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

    // NFT backpack
    public final String NFT_BACKPACK_INSERT = "NFT_BACKPACK_INSERT";
    public final String NFT_BACKPACK_UPDATE = "NFT_BACKPACK_UPDATE";

    // 分发 将nft物品从公共背包分发到A服A角色的玩家背包
    public final String NFT_BACKPACK_DISTRIBUTE = "NFT_BACKPACK_DISTRIBUTE";

    // 收回 撤销分发，收回后的NFT物品在玩家背包内消除
    public final String NFT_BACKPACK_TAKE_BACK = "NFT_BACKPACK_TAKE_BACK";

    // 转移 将已经分发给A区A服下角色的NFT物品转移至B区B服下的角色
    public final String NFT_BACKPACK_TRANSFER = "NFT_BACKPACK_TRANSFER";

    // server role
    public final String SERVER_ROLE_INSERT = "SERVER_ROLE_INSERT";
    public final String SERVER_ROLE_UPDATE = "SERVER_ROLE_UPDATE";
    public final String SERVER_ROLE_DELETE = "SERVER_ROLE_DELETE";

    // NFT MINT
    public final String NFT_MINT_SUCCESS = "NFT_MINT_SUCCESS";

    // skin NFT withdraw
    public final String SKIN_NFT_WITHDRAW = "SKIN_NFT_WITHDRAW";

    // 皮肤NFT首次购买成功
    public final String SKIN_NFT_BUY_SUCCESS_FIRST = "SKIN_NFT_BUY_SUCCESS_FIRST";

    public final String NFT_TRANSFER_SUCCESS_BY_PHANTOM = "NFT_TRANSFER_SUCCESS_BY_PHANTOM";

    public final String SKIN_NFT_LIST_ASSET = "SKIN_NFT_LIST_ASSET";
    public final String SKIN_NFT_CANCEL_ASSET = "SKIN_NFT_CANCEL_ASSET";

    public final String SKIN_NFT_LIST_ASSET_SUCCESS = "SKIN_NFT_LIST_ASSET_SUCCESS";
    public final String SKIN_NFT_CANCEL_ASSET_SUCCESS = "SKIN_NFT_CANCEL_ASSET_SUCCESS";


    // 装备mint 由原来的调用接口换成走Kafka
    public final String EQUIP_MINT_REQUEST = "EQUIP_MINT_REQUEST";
    public final String EQUIP_COMPOSE_REQUEST = "EQUIP_COMPOSE_REQUEST";
}
