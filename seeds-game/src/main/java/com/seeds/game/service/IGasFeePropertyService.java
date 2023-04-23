package com.seeds.game.service;

import com.seeds.common.dto.GenericDto;

import java.math.BigDecimal;

/**
 * @author: hewei
 * @date 2023/4/21
 */
public interface IGasFeePropertyService {
    GenericDto<BigDecimal> getGasFee(Integer type);
}
