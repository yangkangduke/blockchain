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

    BlacklistAddressDto get(Integer type, Long userId, String address);

    /**
     * 从缓存获取
     * @return
     */
    List<BlacklistAddressDto> getAll();

    /**
     * 添加
     * @param blacklistAddressDto
     */
    void add(BlacklistAddressDto blacklistAddressDto);

    /**
     * 更新
     * @param blacklistAddressDto
     */
    void update(BlacklistAddressDto blacklistAddressDto);

    /**
     * 删除
     * @param blacklistAddressDto
     */
    void delete(BlacklistAddressDto blacklistAddressDto);
}
