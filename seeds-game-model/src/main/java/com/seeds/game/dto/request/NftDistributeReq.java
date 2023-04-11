package com.seeds.game.dto.request;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/6
 */
@Data
public class NftDistributeReq {

    /**
     * nft全局id
     */
    private Long autoId;
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
    private String tokenAddress;
    /**
     * 大区名称
     */
    private String regionName;
    /**
     * 游戏服名称
     */
    private String serverName;

    /**
     * 如果是皮肤的话  需要多一个皮肤的图片地址
     */
    private String imgUrl = "";


}
