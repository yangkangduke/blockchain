package com.seeds.uc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.model.UcNftReverseAuction;


/**
 * <p>
 * NFT的反向拍卖 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-10-11
 */
public interface IUcNftReverseAuctionService extends IService<UcNftReverseAuction> {

    /**
     * 根据归属人和NFT的id查询记录
     * @param userId 归属人id
     * @param nftId NFT的id
     * @return 记录
     */
    UcNftReverseAuction queryByUserIdAndNftId(Long userId, Long nftId);

}
