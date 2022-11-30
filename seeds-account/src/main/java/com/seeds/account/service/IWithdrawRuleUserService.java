package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.WithdrawRuleUserDto;
import com.seeds.account.dto.req.WithdrawRuleUserSaveOrUpdateReq;
import com.seeds.account.model.SwitchReq;
import com.seeds.account.model.WithdrawRuleUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提币用户规则 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface IWithdrawRuleUserService extends IService<WithdrawRuleUser> {

    /**
     * 从缓存获取单个
     * @param userId
     * @param currency
     * @param chain
     * @return
     */
    WithdrawRuleUserDto get(long userId, String currency, Integer chain);

    /**
     * 从缓存获取
     * @return
     */
    Map<String, WithdrawRuleUserDto> getAllMap();

    /**
     * 直接从数据库读取所有的合约配置
     * @return
     */
    List<WithdrawRuleUserDto> loadAll();

    /**
     * 插入新的
     * @param req
     */
    Boolean add(WithdrawRuleUserSaveOrUpdateReq req);

    /**
     * 更新
     * @param req
     */
    Boolean update(WithdrawRuleUserSaveOrUpdateReq req);

    /**
     * 启用/停用
     * @param req
     */
    Boolean delete(SwitchReq req);
}

