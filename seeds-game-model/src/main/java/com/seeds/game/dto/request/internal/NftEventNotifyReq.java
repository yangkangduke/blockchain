package com.seeds.game.dto.request.internal;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/26
 */
@Data
public class NftEventNotifyReq {
    // 图纸全局唯一id
    private Long autoId;
    // 玩家id
    private Long accId;
    // 道具id
    private Long configId;
    // 操作类型：1mint成功,2取消
    private Integer optType;
    // tokenAddress（mint成功时传）
    private String tokenAddress;
    // 大区名字（mint成功时传）
    private String regionName;
    // 游戏服名字（mint成功时传）
    private String serverName;
    // 1 装备2道具3英雄
    private Integer type;
    // 3 托管  4 未托管
    private Integer state;
}
