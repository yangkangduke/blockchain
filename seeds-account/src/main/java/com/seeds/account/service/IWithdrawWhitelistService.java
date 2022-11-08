package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.WithdrawWhitelistDto;
import com.seeds.account.model.WithdrawWhitelist;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提币白名单 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface IWithdrawWhitelistService extends IService<WithdrawWhitelist> {

    /**
     * 从缓存获取单个
     * @param userId
     * @param currency
     * @return
     */
    WithdrawWhitelistDto get(long userId, String currency);

    /**
     * 从缓存获取
     * @return
     */
    Map<String, WithdrawWhitelistDto> getAllMap();

    /**
     * 直接从数据库读取所有的合约配置
     * @return
     */
    List<WithdrawWhitelistDto> loadAll();

    /**
     * 插入新的
     * @param withdrawWhitelistDto
     */
    void add(WithdrawWhitelistDto withdrawWhitelistDto);

    /**
     * 更新
     * @param withdrawWhitelistDto
     */
    void update(WithdrawWhitelistDto withdrawWhitelistDto);
}

