package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.NftReferencePrice;


/**
 * <p>
 * nft参考价
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
public interface INftReferencePriceService extends IService<NftReferencePrice> {

    /**
     * 查询最新的一条记录
     * @return 最新的一条记录
     */
    NftReferencePrice queryOneNewestRecord();

}
