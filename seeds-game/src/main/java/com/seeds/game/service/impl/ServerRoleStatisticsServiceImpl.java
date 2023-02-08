package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.ServerRoleStatisticsEntity;
import com.seeds.game.mapper.ServerRoleStatisticsMapper;
import com.seeds.game.service.IServerRoleStatisticsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏服角色统计 服务实现类
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-08
 */
@Service
public class ServerRoleStatisticsServiceImpl extends ServiceImpl<ServerRoleStatisticsMapper, ServerRoleStatisticsEntity> implements IServerRoleStatisticsService {

    @Override
    public ServerRoleStatisticsEntity queryByRoleId(Long roleId) {
        LambdaQueryWrapper<ServerRoleStatisticsEntity> wrapper = new QueryWrapper<ServerRoleStatisticsEntity>().lambda()
                .eq(ServerRoleStatisticsEntity::getRoleId, roleId);
        return getOne(wrapper);
    }
}
