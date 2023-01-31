package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.dto.request.internal.ServerRoleCreateUpdateReq;
import com.seeds.game.dto.request.internal.ServerRolePageReq;
import com.seeds.game.dto.response.ServerRoleResp;
import com.seeds.game.entity.ServerRoleEntity;
import com.seeds.game.mapper.ServerRoleMapper;
import com.seeds.game.service.IServerRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class ServerRoleServiceImpl extends ServiceImpl<ServerRoleMapper, ServerRoleEntity> implements IServerRoleService {

    @Override
    public IPage<ServerRoleResp> queryPage(ServerRolePageReq req) {

        LambdaQueryWrapper<ServerRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ServerRoleEntity::getLevel);
        Page<ServerRoleEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<ServerRoleEntity> records = page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            ServerRoleResp resp = new ServerRoleResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public void createRole(ServerRoleCreateUpdateReq req) {
        ServerRoleEntity entity = new ServerRoleEntity();
        BeanUtils.copyProperties(req, entity);
        this.save(entity);
    }

    @Override
    public void updateRole(ServerRoleCreateUpdateReq req) {
        ServerRoleEntity entity = new ServerRoleEntity();
        BeanUtils.copyProperties(req, entity);
        this.updateById(entity);
    }

    @Override
    public void delete(Long id) {
        this.removeById(id);
    }
}
