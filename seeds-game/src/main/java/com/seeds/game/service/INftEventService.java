package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.internal.NftEventAddReq;
import com.seeds.game.dto.request.internal.NftEventPageReq;
import com.seeds.game.dto.response.NftEventResp;
import com.seeds.game.dto.response.TypeNum;
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

    List<TypeNum> getTypeNum(Long userId);

    void OptSuccess(Long eventId, String tokenId, Integer autoDeposite);
}
