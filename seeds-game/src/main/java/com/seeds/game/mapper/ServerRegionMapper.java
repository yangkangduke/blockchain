package com.seeds.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.game.dto.response.ServeRoleRegionResp;
import com.seeds.game.entity.ServerRegionEntity;

import java.util.List;

/**
 * <p>
 * 游戏区服 Mapper 接口
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-16
 */
public interface ServerRegionMapper extends BaseMapper<ServerRegionEntity> {

    List<ServeRoleRegionResp> getListForSelf(Long userId);
}
