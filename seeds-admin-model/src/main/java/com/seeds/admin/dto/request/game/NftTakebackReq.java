package com.seeds.admin.dto.request.game;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/6
 */
@Data
public class NftTakebackReq {

    /**
     * nft全局id
     */
    private Integer autoId;
    /**
     * 游戏服务角色id（serverRoleId）
     */
    private Long accId;
    /**
     * 静态表id（itemId）
     */
    private Integer configId;
    /**
     * 类型：1.装备 2.道具 3.英雄
     */
    private Integer type;
    /**
     * nft token
     */
    private String tokenId;

}
