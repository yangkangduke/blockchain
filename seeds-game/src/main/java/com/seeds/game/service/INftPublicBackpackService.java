package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackPageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackTakeBackReq;
import com.seeds.game.dto.response.NftPublicBackpackResp;
import com.seeds.game.dto.response.OpenNftPublicBackpackDisResp;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * NFT公共背包 服务类
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
public interface INftPublicBackpackService extends IService<NftPublicBackpackEntity> {

    IPage<NftPublicBackpackResp> queryPage(NftPublicBackpackPageReq req);

    void create(NftPublicBackpackReq req);

    void update(NftPublicBackpackReq req);

    NftPublicBackpackResp detail(Integer autoId);

    OpenNftPublicBackpackDisResp distribute(NftPublicBackpackDisReq req);

    void takeBack(NftPublicBackpackTakeBackReq req);

    OpenNftPublicBackpackDisResp transfer(NftPublicBackpackDisReq req);

    List<NftPublicBackpackResp> queryList(OpenNftPublicBackpackPageReq req);
}
