package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.ServerRoleStatisticsEntity;

/**
 * <p>
 * 游戏服角色统计 服务类
 * </p>
 *
 * @author hang。yu
 * @since 2023-02-02
 */
public interface IServerRoleStatisticsService extends IService<ServerRoleStatisticsEntity> {

    /**
     * 获取游戏角色统计
     * @param roleId 游戏角色id
     * @return 游戏角色统计
     */
    ServerRoleStatisticsEntity queryByRoleId(Long roleId);

}
