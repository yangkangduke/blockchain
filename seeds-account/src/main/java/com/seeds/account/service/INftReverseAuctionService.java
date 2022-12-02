package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.model.NftReverseAuction;


/**
 * <p>
 * NFT的反向拍卖 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-10-11
 */
public interface INftReverseAuctionService extends IService<NftReverseAuction> {

    /**
     * 根据归属人和NFT的id查询记录
     * @param userId 归属人id
     * @param nftId NFT的id
     * @return 记录
     */
    NftReverseAuction queryByUserIdAndNftId(Long userId, Long nftId);

}
