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
     * 根据订单id或拍卖id查询记录
     * @param orderId 订单id
     * @return 托管费记录
     */
    NftFeeRecordEntity queryByOrderId(Long orderId);

    /**
     * 根据订单id或拍卖id查询记录
     * @param auctionId 拍卖id
     * @return 托管费记录
     */
    NftFeeRecordEntity queryByAuctionId(Long auctionId);

}
