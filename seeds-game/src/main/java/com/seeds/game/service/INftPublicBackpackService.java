package com.seeds.game.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.common.dto.GenericDto;
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

    Map<Long, String> queryTokenAddressByAutoIds(List<Long> autoIds);

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

    GenericDto<Object> depositCheck(NftDepositCheckReq req);

    /**
     * 根据指定字段批量更新
     *
     * @param entityList
     * @param wrapperFunction
     * @return
     */
    boolean updateBatchByQueryWrapper(Collection<NftPublicBackpackEntity> entityList, Function<NftPublicBackpackEntity, LambdaQueryWrapper> wrapperFunction);

    void skinUnDeposited(NftUnDepositedReq req);

    void insertBackpack(List<NftPublicBackpackDto> backpackEntities);

    Map<String, List<SkinNftTypeResp>> getSkinNftTypeList(Integer heroType);

    List<NftPublicBackpackSkinWebResp> getSkinPageForWeb(NftBackpackWebPageReq req);

    SysNftPicEntity getHeroAndSkin(Long nftPicId);

    /**
     * 通过NFT编号集合获取背包数据
     * @param eqNftIds NFT编号集合
     * @return 背包数据
     */
    Map<Long, NftPublicBackpackEntity> queryMapByEqNftIds(Collection<Long> eqNftIds);

    /**
     * 通过NFT地址集合获取背包数据
     * @param mintAddresses NFT地址集合
     * @return 背包数据
     */
    Map<String, NftPublicBackpackEntity> queryMapByMintAddress(Collection<String> mintAddresses);

    /**
     * 通过NFT地址集合获取背包数据
     * @param mintAddresses NFT地址集合
     * @return 背包数据
     */
    List<NftPublicBackpackEntity> queryItemsByMintAddress(Collection<String> mintAddresses);

    String getTokenAddress(String mintAddress, String ownerAddress);

    /**
     * 通过道具id集合获取背包数据
     * @param itemIds 道具id集合
     * @return 背包数据
     */
    List<NftPublicBackpackEntity> queryByItemIds(Collection<Long> itemIds);

}
