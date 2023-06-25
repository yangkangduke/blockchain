package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftReferencePrice;
import com.seeds.game.mapper.NftReferencePriceMapper;
import com.seeds.game.service.INftReferencePriceService;
import org.springframework.stereotype.Service;


/**
 * <p>
 * nft参考价
 * </p>
 *
 * @author hang.yu
 * @since 2023-06-25
 */
@Service
public class NftReferencePriceServiceImpl extends ServiceImpl<NftReferencePriceMapper, NftReferencePrice> implements INftReferencePriceService {

    @Override
    public NftReferencePrice queryOneNewestRecord() {
        return getOne(new LambdaQueryWrapper<NftReferencePrice>()
                .orderByDesc(NftReferencePrice::getUpdateTime)
                .last(" limit 1"));
    }
}
