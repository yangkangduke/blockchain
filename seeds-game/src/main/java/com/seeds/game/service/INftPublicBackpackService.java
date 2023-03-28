package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.NftDepositedReq;
import com.seeds.game.dto.request.NftUnDepositedReq;
import com.seeds.game.dto.request.OpenNftOwnershipTransferReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackPageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackTakeBackReq;
import com.seeds.game.dto.response.NftPublicBackpackResp;
import com.seeds.game.dto.response.NftType;
import com.seeds.game.dto.response.NftTypeNum;
import com.seeds.game.dto.response.OpenNftPublicBackpackDisResp;
import com.seeds.game.entity.NftPublicBackpackEntity;

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

    List<NftPublicBackpackResp> queryList(NftPublicBackpackPageReq req);

    NftPublicBackpackEntity detailForTokenId(String tokenId);

    void ownerTransfer(OpenNftOwnershipTransferReq req);

    /**
     * nft托管
     * @param req 验证
     */
    void deposited(NftDepositedReq req);

    /**
     * 取消nft托管
     *
     * @param req 验证
     */
    void unDeposited(NftUnDepositedReq req);

    /**
     * 获取各个分类的数量
     *
     * @return
     */
    List<NftTypeNum> typeNum();

    /**
     * 获取各个分类的列表
     *
     * @return
     */
    List<NftType> getNftTypeList();
}
