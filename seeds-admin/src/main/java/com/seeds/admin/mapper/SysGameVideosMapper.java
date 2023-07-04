package com.seeds.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysGameVideosReq;
import com.seeds.admin.dto.response.SysGameVideosResp;
import com.seeds.admin.entity.SysGameVideosEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 游戏视频管理 Mapper 接口
 * </p>
 *
 * @author hewei
 * @since 2023-06-25
 */
@Repository
public interface SysGameVideosMapper extends BaseMapper<SysGameVideosEntity> {
    IPage<SysGameVideosResp> getPage(Page page, @Param("params") SysGameVideosReq req);
}
