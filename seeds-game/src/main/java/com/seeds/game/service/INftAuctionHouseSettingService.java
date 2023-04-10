package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.NftAuctionHouseSetting;

/**
 * <p>
 * nft拍卖配置
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
public interface INftAuctionHouseSettingService extends IService<NftAuctionHouseSetting> {

    /**
     * 查询拍卖配置
     * @param listingId auction list表id
     * @return 拍卖配置
     */
    NftAuctionHouseSetting queryByListingId(Long listingId);

}
