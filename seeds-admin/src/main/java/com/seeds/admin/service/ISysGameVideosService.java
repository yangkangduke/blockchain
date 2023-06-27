package com.seeds.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysGameVideoAddOrModifyReq;
import com.seeds.admin.dto.request.SysGameVideosReq;
import com.seeds.admin.dto.response.SysGameVideosResp;
import com.seeds.admin.entity.SysGameVideosEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 游戏视频管理 服务类
 * </p>
 *
 * @author hewei
 * @since 2023-06-25
 */
public interface ISysGameVideosService extends IService<SysGameVideosEntity> {

    IPage<SysGameVideosResp> queryPage(SysGameVideosReq req);

    void add(SysGameVideoAddOrModifyReq req);

    SysGameVideosResp detail(Long id);

    void modify(SysGameVideoAddOrModifyReq req);

    void delete(ListReq req);

    List<SysGameVideosResp> getTopVideos();

    void onShelves(SysGameVideoAddOrModifyReq req);

    void top(SysGameVideoAddOrModifyReq req);
}
