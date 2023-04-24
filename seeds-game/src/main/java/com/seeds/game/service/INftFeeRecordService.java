package com.seeds.game.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.NftFeeRecordEntity;

/**
 * <p>
 * nft托管费记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-04-19
 */
public interface INftFeeRecordService extends IService<NftFeeRecordEntity> {

    /**
     * 根据托管费hash和NFT铸造地址查询记录
     * @param mintAddress NFT铸造地址
     * @param feeHash 托管费hash
     * @return 托管费记录
     */
    NftFeeRecordEntity queryByMintAddressAndFeeHash(String mintAddress, String feeHash);

    /**
     * 根据订单id查询记录
     * @param orderId 订单id
     * @return 托管费记录
     */
    NftFeeRecordEntity queryByOrderId(Long orderId);

    /**
     * 根据拍卖id查询记录
     * @param auctionId 拍卖id
     * @return 托管费记录
     */
    NftFeeRecordEntity queryByAuctionId(Long auctionId);

}
