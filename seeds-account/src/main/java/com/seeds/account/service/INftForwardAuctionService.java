package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.model.NftForwardAuction;


/**
 * <p>
 * NFT的正向拍卖 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-05
 */
public interface INftForwardAuctionService extends IService<NftForwardAuction> {

    /**
     * 根据归属人和NFT的id查询记录
     * @param userId 归属人id
     * @param nftId NFT的id
     * @return 记录
     */
    NftForwardAuction queryByUserIdAndNftId(Long userId, Long nftId);

}
