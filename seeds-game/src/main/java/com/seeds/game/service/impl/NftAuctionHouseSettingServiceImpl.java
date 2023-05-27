package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftAuctionHouseSetting;
import com.seeds.game.mapper.NftAuctionHouseSettingMapper;
import com.seeds.game.service.INftAuctionHouseSettingService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * nft拍卖配置
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
@Service
public class NftAuctionHouseSettingServiceImpl extends ServiceImpl<NftAuctionHouseSettingMapper, NftAuctionHouseSetting> implements INftAuctionHouseSettingService {

    @Override
    public NftAuctionHouseSetting queryByListingId(Long listingId) {
        return getOne(new LambdaQueryWrapper<NftAuctionHouseSetting>().eq(NftAuctionHouseSetting::getListingId, listingId));
    }

    @Override
    public Map<Long, NftAuctionHouseSetting> queryMapByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<NftAuctionHouseSetting> list = listByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(NftAuctionHouseSetting::getId, p -> p));
    }
}
