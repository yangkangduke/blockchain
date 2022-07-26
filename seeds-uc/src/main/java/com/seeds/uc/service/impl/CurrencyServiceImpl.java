package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.mapper.CurrencyMapper;
import com.seeds.uc.model.Currency;
import com.seeds.uc.service.ICurrencyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 币种信息 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Service
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, Currency> implements ICurrencyService {

}
