package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.NftReferencePrice;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;


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

    /**
     * 通过类型和编号查询高等级的记录
     * @param itemId 道具id
     * @return 高等级的记录
     */
    List<NftReferencePrice> queryByTypeAndHighGradeNoAvg(Long itemId);

    /**
     * 通过类型和编号查询低等级的记录
     * @param itemId 道具id
     * @return 低等级的记录
     */
    BigDecimal queryLowGradeAveragePrice(Long itemId);

    /**
     * 通过id合集查询记录
     * @param ids id合集
     * @return 记录
     */
    Map<Long, NftReferencePrice> queryMapByIds(Collection<Long> ids);

}
