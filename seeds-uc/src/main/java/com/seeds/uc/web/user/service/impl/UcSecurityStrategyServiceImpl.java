package com.seeds.uc.web.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.model.user.entity.UcSecurityStrategy;
import com.seeds.uc.web.user.mapper.UcSecurityStrategyMapper;
import com.seeds.uc.web.user.service.IUcSecurityStrategyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 安全策略表 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-07-15
 */
@Service
public class UcSecurityStrategyServiceImpl extends ServiceImpl<UcSecurityStrategyMapper, UcSecurityStrategy> implements IUcSecurityStrategyService {

}
