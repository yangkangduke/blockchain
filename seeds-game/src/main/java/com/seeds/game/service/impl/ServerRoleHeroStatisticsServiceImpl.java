package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.ServerRoleHeroStatisticsEntity;
import com.seeds.game.mapper.ServerRoleHeroStatisticsMapper;
import com.seeds.game.service.IServerRoleHeroStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 游戏服角色的英雄统计 服务实现类
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-08
 */
@Service
public class ServerRoleHeroStatisticsServiceImpl extends ServiceImpl<ServerRoleHeroStatisticsMapper, ServerRoleHeroStatisticsEntity> implements IServerRoleHeroStatisticsService {

    @Override
    public List<ServerRoleHeroStatisticsEntity> queryByRoleId(Long roleId) {
        LambdaQueryWrapper<ServerRoleHeroStatisticsEntity> wrapper = new QueryWrapper<ServerRoleHeroStatisticsEntity>().lambda()
                .eq(ServerRoleHeroStatisticsEntity::getRoleId, roleId);
        return list(wrapper);
    }
}
