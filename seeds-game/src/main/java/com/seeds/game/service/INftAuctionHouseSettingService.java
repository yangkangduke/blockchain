package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.NftAuctionHouseSetting;

import java.util.Collection;
import java.util.Map;

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

    /**
     * 根据拍卖id查询拍卖上架记录
     * @param ids 拍卖id
     * @return 拍卖上架记录
     */
    Map<Long, NftAuctionHouseSetting> queryMapByIds(Collection<Long> ids);

}
