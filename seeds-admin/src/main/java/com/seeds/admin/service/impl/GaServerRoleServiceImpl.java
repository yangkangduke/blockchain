package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.GaServerRoleEntity;
import com.seeds.admin.mapper.GaServerRoleMapper;
import com.seeds.admin.service.GaServerRoleService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 游戏服角色 服务实现类
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@Service
public class GaServerRoleServiceImpl extends ServiceImpl<GaServerRoleMapper, GaServerRoleEntity> implements GaServerRoleService {

    @Override
    public List<GaServerRoleEntity> queryByUserId(Long userId) {
        LambdaQueryWrapper<GaServerRoleEntity> wrapper = new QueryWrapper<GaServerRoleEntity>().lambda()
                .eq(GaServerRoleEntity::getUserId, userId);
        return list(wrapper);
    }
}
