package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.entity.GaServerRoleEntity;

import java.util.List;


/**
 * <p>
 * 游戏服角色 服务类
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
public interface GaServerRoleService extends IService<GaServerRoleEntity> {

    /**
     * 通过用户id获取游戏角色列表
     * @param userId 用户id
     * @return 游戏角色列表
     */
    List<GaServerRoleEntity> queryByUserId(Long userId);

}
