package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.OpenServerRegionCreateUpdateReq;
import com.seeds.game.dto.response.ServerRegionResp;
import com.seeds.game.entity.ServerRegionEntity;

import java.util.List;

/**
 * <p>
 * 游戏区服 服务类
 * </p>
 *
 * @author hang。yu
 * @since 2023-02-16
 */
public interface IServerRegionService extends IService<ServerRegionEntity> {

    /**
     * 获取游戏区服
     * @return 游戏区服
     */
    List<ServerRegionResp> queryList();

    void createOrUpdate(OpenServerRegionCreateUpdateReq req);
}
