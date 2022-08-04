package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.dto.response.UcSecurityStrategyResp;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.mapper.UcSecurityStrategyMapper;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IUcSecurityStrategyService;
import com.seeds.uc.service.IUcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 安全策略表 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-07-15
 */
@Service
@Slf4j
@Transactional
public class UcSecurityStrategyServiceImpl extends ServiceImpl<UcSecurityStrategyMapper, UcSecurityStrategy> implements IUcSecurityStrategyService {

    /**
     * 根据用户id获取用户的策略
     * @param userId
     * @return
     */
    @Override
    public List<UcSecurityStrategyResp> getByUserId(Long userId) {
        return baseMapper.getByUserId(userId);
    }


}
