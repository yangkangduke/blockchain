package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.OpenServerRoleCreateUpdateReq;
import com.seeds.game.dto.request.OpenServerRolePageReq;
import com.seeds.game.dto.request.internal.DeleteReq;
import com.seeds.game.dto.request.internal.ServerRoleCreateUpdateReq;
import com.seeds.game.dto.request.internal.ServerRolePageReq;
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

    IPage<ServerRoleResp> queryPage(ServerRolePageReq req);

    void createRole(ServerRoleCreateUpdateReq req);

    void updateRole(ServerRoleCreateUpdateReq req);

    void delete(DeleteReq req);
}
