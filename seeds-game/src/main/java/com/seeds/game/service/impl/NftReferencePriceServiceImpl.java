package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftReferencePrice;
import com.seeds.game.mapper.NftReferencePriceMapper;
import com.seeds.game.service.INftReferencePriceService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    @Override
    public List<NftReferencePrice> queryByTypeAndHighGradeNoAvg(Long itemId) {
        Long typeId = Long.valueOf(itemId.toString().substring(0, 4));
        Integer grade = Integer.valueOf(itemId.toString().substring(4, 6));
        String number = itemId.toString().substring(6);
        return list(new LambdaQueryWrapper<NftReferencePrice>()
                .eq(NftReferencePrice::getTypeId, typeId)
                .ge(NftReferencePrice::getGrade, grade)
                .ne(NftReferencePrice::getNumber, number)
                .isNull(NftReferencePrice::getAveragePrice));
    }

    @Override
    public BigDecimal queryLowGradeAveragePrice(Long itemId) {
        NftReferencePrice one = getById(itemId);
        if (one != null) {
            return one.getAveragePrice() == null ? one.getReferencePrice() : one.getAveragePrice();
        }
        Long typeId = Long.valueOf(itemId.toString().substring(0, 4));
        Integer grade = Integer.valueOf(itemId.toString().substring(4, 6));
        String number = itemId.toString().substring(6);
        List<NftReferencePrice> list = list(new LambdaQueryWrapper<NftReferencePrice>()
                .eq(NftReferencePrice::getTypeId, typeId)
                .le(NftReferencePrice::getGrade, grade)
                .isNotNull(NftReferencePrice::getAveragePrice)
                .orderByDesc(NftReferencePrice::getGrade));
        NftReferencePrice price = list.get(0);
        for (NftReferencePrice referencePrice : list) {
            if (!referencePrice.getGrade().equals(price.getGrade())) {
                break;
            }
            if (price.getAveragePrice().compareTo(referencePrice.getAveragePrice()) > 0) {
                price = referencePrice;
            }
        }
        one = new NftReferencePrice();
        long currentTimeMillis = System.currentTimeMillis();
        one.setId(itemId);
        one.setTypeId(typeId);
        one.setGrade(grade);
        one.setNumber(number);
        one.setCreateTime(currentTimeMillis);
        one.setUpdateTime(0L);
        double difference = Math.pow(3, grade - price.getGrade());
        BigDecimal unitPrice = new BigDecimal(difference).multiply(price.getAveragePrice());
        one.setReferencePrice(unitPrice);
        save(one);
        return price.getAveragePrice();
    }

    @Override
    public Map<Long, NftReferencePrice> queryMapByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<NftReferencePrice> list = listByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(NftReferencePrice::getId, p -> p));
    }
}
