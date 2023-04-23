package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.entity.PricePropertyEntity;
import com.seeds.game.mapper.GasFeePropertyMapper;
import com.seeds.game.service.IGasFeePropertyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author: hewei
 * @date 2023/4/21
 */
@Service
public class GasFeePropertyServiceImpl extends ServiceImpl<GasFeePropertyMapper, PricePropertyEntity> implements IGasFeePropertyService {

    @Override
    public GenericDto<BigDecimal> getGasFee(Integer type) {
        BigDecimal gasFee = new BigDecimal(0);
        PricePropertyEntity one = this.getOne(new LambdaQueryWrapper<PricePropertyEntity>().eq(PricePropertyEntity::getType, type));
        if (Objects.nonNull(one)) {
            gasFee = one.getGasFee();
        }
        return GenericDto.success(gasFee);
    }
}
