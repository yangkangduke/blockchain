package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.OpenServerRoleCreateUpdateReq;
import com.seeds.game.dto.request.OpenServerRolePageReq;
import com.seeds.game.dto.response.ServerRoleResp;
import com.seeds.game.entity.ServerRoleEntity;

/**
 * <p>
 * 游戏服角色 服务类
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
public interface IServerRoleService extends IService<ServerRoleEntity> {

    IPage<ServerRoleResp> queryPage(OpenServerRolePageReq req);

    void createRole(OpenServerRoleCreateUpdateReq req);

    void updateRole(OpenServerRoleCreateUpdateReq req);

    void delete(Long id);
}
