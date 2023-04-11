package com.seeds.game.dto.response;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/29
 */
@Data
public class ServeRoleRegionResp {
    // 角色id
    private Long serverRoleId;
    // 角色名字
    private String roleName;
    // 角色等级
    private Integer roleLevel;
    // 大区名字
    private String regionName;
    // 游戏服名字
    private String gameServerName;
    // 大区
    private Integer region;
    // 游戏服
    private Integer gameServer;
}
