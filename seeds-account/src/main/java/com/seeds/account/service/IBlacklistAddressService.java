package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.account.model.BlacklistAddress;

import java.util.List;

/**
 * <p>
 * Ethereum黑地址 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface IBlacklistAddressService extends IService<BlacklistAddress> {

    /**
     * 直接从数据库读取所有的合约配置
     * @return
     */
    List<BlacklistAddressDto> loadAll();

    BlacklistAddressDto get(int type, long userId, String address);

    /**
     * 从缓存获取
     * @return
     */
    List<BlacklistAddressDto> getAll();
}