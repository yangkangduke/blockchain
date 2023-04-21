package com.seeds.game.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.NftMarketOrderEntity;

import java.util.List;

public interface INftMarketOrderService extends IService<NftMarketOrderEntity> {

    /**
     * 通过nft地址查询挂单的NFT
     * @param mintAddress nft地址
     * @param status 订单状态
     * @return 挂单的NFT
     */
    NftMarketOrderEntity queryByMintAddressAndStatus(String mintAddress, Integer status);

    /**
     * 查询用户交易记录
     * @param publicAddress 公共地址
     * @param status 订单状态
     * @return 交易记录
     */
    List<NftMarketOrderEntity> queryUserTradesByAddressAndStatus(String publicAddress, Integer status);

    /**
     * 通过拍卖id查询NFT订单
     * @param auctionId 拍卖id
     * @return NFT订单
     */
    NftMarketOrderEntity queryByAuctionId(Long auctionId);

}
