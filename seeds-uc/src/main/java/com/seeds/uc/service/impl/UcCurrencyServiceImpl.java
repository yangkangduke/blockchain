package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.mapper.UcCurrencyMapper;
import com.seeds.uc.model.UcCurrency;
import com.seeds.uc.service.IUcCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 币种信息 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Service
@Slf4j
@Transactional
public class UcCurrencyServiceImpl extends ServiceImpl<UcCurrencyMapper, UcCurrency> implements IUcCurrencyService {

}
