package com.seeds.uc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.model.UcNftForwardAuction;


/**
 * <p>
 * NFT的正向拍卖 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-05
 */
public interface IUcNftForwardAuctionService extends IService<UcNftForwardAuction> {

    /**
     * 根据归属人和NFT的id查询记录
     * @param userId 归属人id
     * @param nftId NFT的id
     * @return 记录
     */
    UcNftForwardAuction queryByUserIdAndNftId(Long userId, Long nftId);

}
