package com.seeds.uc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.dto.response.UcSecurityStrategyResp;
import com.seeds.uc.model.UcSecurityStrategy;

import java.util.List;

/**
 * <p>
 * 安全策略表 服务类
 * </p>
 *
 * @author yk
 * @since 2022-07-15
 */
public interface IUcSecurityStrategyService extends IService<UcSecurityStrategy> {

    /**
     * 根据用户id获取用户的策略
     * @param userId
     * @return
     */
    List<UcSecurityStrategyResp> getByUserId(Long userId);
}
