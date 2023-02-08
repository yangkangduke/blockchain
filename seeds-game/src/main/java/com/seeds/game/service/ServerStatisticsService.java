package com.seeds.game.service;


import com.seeds.game.dto.response.ServerRoleHeroStatisticsResp;
import com.seeds.game.dto.response.ServerRoleStatisticsResp;

import java.util.List;

/**
 * <p>
 * 游戏服统计 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-02
 */
public interface ServerStatisticsService {

    /**
     * 获取游戏角色统计
     * @param currentUserId 登录UC用户id
     * @param region 区域
     * @param server 服务器
     * @return 游戏角色统计
     */
    ServerRoleStatisticsResp roleData(Long currentUserId, Integer region, Integer server);

    /**
     * 获取游戏角色英雄统计
     * @param currentUserId 登录UC用户id
     * @param region 区域
     * @param server 服务器
     * @return 游戏角色英雄统计
     */
    List<ServerRoleHeroStatisticsResp> roleHeroData(Long currentUserId, Integer region, Integer server);

}
