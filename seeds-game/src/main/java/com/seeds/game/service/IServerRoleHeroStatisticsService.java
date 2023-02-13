package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.ServerRoleHeroStatisticsEntity;

import java.util.List;


/**
 * <p>
 * 游戏服角色的英雄统计 服务类
 * </p>
 *
 * @author hang。yu
 * @since 2023-02-02
 */
public interface IServerRoleHeroStatisticsService extends IService<ServerRoleHeroStatisticsEntity> {

    /**
     * 获取游戏角色的英雄统计
     * @param roleId 游戏角色id
     * @return 游戏角色的英雄统计
     */
    List<ServerRoleHeroStatisticsEntity> queryByRoleId(Long roleId);

}
