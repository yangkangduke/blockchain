package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.ComposeSuccessReq;
import com.seeds.game.dto.request.NftMintSuccessReq;
import com.seeds.game.dto.request.internal.NftEventAddReq;
import com.seeds.game.dto.request.internal.NftEventPageReq;
import com.seeds.game.dto.response.NftEventResp;
import com.seeds.game.dto.response.EventTypeNum;
import com.seeds.game.entity.NftEvent;

import java.util.List;

/**
 * <p>
 * nft通知 服务类
 * </p>
 *
 * @author hewei
 * @since 2023-03-23
 */
public interface INftEventService extends IService<NftEvent> {

    IPage<NftEventResp> getPage(NftEventPageReq req);

    Long getHandleFlag(Long userId);

    void toNft(NftEventAddReq req);

    Boolean delete(Long id);

    Boolean cancel(Long id);

    List<EventTypeNum> getTypeNum(Long userId);

    void OptSuccess(NftMintSuccessReq mintSuccessReq);

    void composeSuccess(ComposeSuccessReq req);
}
