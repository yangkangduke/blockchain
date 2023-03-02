package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.dto.request.internal.DeleteReq;
import com.seeds.game.dto.request.internal.ServerRoleCreateUpdateReq;
import com.seeds.game.dto.request.internal.ServerRolePageReq;
import com.seeds.game.dto.response.ServerRoleResp;
import com.seeds.game.entity.ServerRegionEntity;
import com.seeds.game.entity.ServerRoleEntity;
import com.seeds.game.mapper.ServerRoleMapper;
import com.seeds.game.service.IServerRegionService;
import com.seeds.game.service.IServerRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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


    @Autowired
    private IServerRegionService serverRegionService;

    @Override
    public IPage<ServerRoleResp> queryPage(ServerRolePageReq req) {

        LambdaQueryWrapper<ServerRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServerRoleEntity::getUserId, req.getUserId())
                .orderByDesc(ServerRoleEntity::getLevel);

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
    public void createOrUpdate(ServerRoleCreateUpdateReq req) {
        ServerRoleEntity serverRole = this.getById(req.getId());
        if (Objects.isNull(serverRole)) {
            ServerRoleEntity entity = new ServerRoleEntity();
            BeanUtils.copyProperties(req, entity);
            entity.setCreatedAt(System.currentTimeMillis());
            entity.setCreatedBy(req.getUserId());
            entity.setUpdatedAt(System.currentTimeMillis());
            entity.setUpdatedBy(req.getUserId());
            this.save(entity);
        } else {
            ServerRoleEntity entity = new ServerRoleEntity();
            BeanUtils.copyProperties(req, entity);
            entity.setUpdatedAt(System.currentTimeMillis());
            entity.setUpdatedBy(req.getUserId());
            this.updateById(entity);
        }

    }

    @Override
    public void updateRole(ServerRoleCreateUpdateReq req) {
        ServerRoleEntity entity = new ServerRoleEntity();
        BeanUtils.copyProperties(req, entity);
        entity.setUpdatedAt(System.currentTimeMillis());
        entity.setUpdatedBy(req.getUserId());
        this.updateById(entity);
    }

    @Override
    public void delete(DeleteReq req) {
        ServerRoleEntity entity = getById(req.getId());

        if (entity.getUserId().equals(req.getUserId())) {
            this.removeById(req.getId());
        }
    }

    @Override
    public List<ServerRoleResp> queryList(ServerRolePageReq req) {

        List<ServerRoleResp> respList = new ArrayList<>();
        LambdaQueryWrapper<ServerRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServerRoleEntity::getUserId, req.getUserId())
                .orderByDesc(ServerRoleEntity::getLevel);
        List<ServerRoleEntity> list = this.list(wrapper);
        if (!CollectionUtils.isEmpty(list)) {
            respList = list.stream().map(p -> {
                ServerRoleResp resp = new ServerRoleResp();
                BeanUtils.copyProperties(p, resp);

                ServerRegionEntity one = serverRegionService.getOne(new LambdaQueryWrapper<ServerRegionEntity>()
                        .eq(ServerRegionEntity::getRegion, p.getRegion())
                        .eq(ServerRegionEntity::getGameServer, p.getGameServer()));
                if (Objects.nonNull(one)) {
                    resp.setRegionName(one.getRegionName());
                    resp.setGameServerName(one.getGameServerName());
                }
                return resp;
            }).collect(Collectors.toList());

        }
        return respList;
    }

    @Override
    public ServerRoleEntity queryByUserIdAndRegionAndServer(Long userId, Integer region, Integer server) {
        LambdaQueryWrapper<ServerRoleEntity> wrapper = new QueryWrapper<ServerRoleEntity>().lambda()
                .eq(ServerRoleEntity::getUserId, userId)
                .eq(ServerRoleEntity::getRegion, region)
                .eq(ServerRoleEntity::getGameServer, server);
        return getOne(wrapper);
    }
}
