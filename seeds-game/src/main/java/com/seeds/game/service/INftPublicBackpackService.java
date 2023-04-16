package com.seeds.game.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.request.internal.*;
import com.seeds.game.dto.response.*;
import com.seeds.game.entity.NftPublicBackpackEntity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    Integer distributeBatch(List<NftPublicBackpackDisReq> reqs);

    Integer takeBackBatch(List<NftPublicBackpackTakeBackReq> reqs);

    Integer transferBatch(List<NftPublicBackpackDisReq> reqs);

    OpenNftPublicBackpackDisResp distribute(NftPublicBackpackDisReq req);

    void takeBack(NftPublicBackpackTakeBackReq req);

    OpenNftPublicBackpackDisResp transfer(NftPublicBackpackDisReq req);

    List<NftPublicBackpackResp> queryList(NftPublicBackpackPageReq req);

    NftPublicBackpackEntity queryByMintAddress(String mintAddress);

    NftPublicBackpackEntity queryByTokenId(String tokenId);

    String queryTokenAddressByAutoId(Long autoId);

    void ownerTransfer(OpenNftOwnershipTransferReq req);

    /**
     * 通过NFT id查询NFT
     * @param eqNftId NFT id
     * @return  NFT
     */
    NftPublicBackpackEntity queryByEqNftId(Long eqNftId);

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
    List<NftType> getNftTypeList(Integer type);

    List<NftPublicBackpackWebResp> getPageForWeb(NftBackpackWebPageReq req);

    void updateState(NftBackpakcUpdateStateReq req);

    void insertCallback(MintSuccessReq req);

    Map<Long, BigDecimal> getTotalPrice(String autoIds);

    Boolean depositCheck(NftDepositCheckReq req);

    /**
     * 根据指定字段批量更新
     * @param entityList
     * @param wrapperFunction
     * @return
     */
    boolean updateBatchByQueryWrapper(Collection<NftPublicBackpackEntity> entityList, Function<NftPublicBackpackEntity, LambdaQueryWrapper> wrapperFunction);

}
