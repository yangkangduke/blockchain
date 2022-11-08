package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.SystemWalletAddressDto;
import com.seeds.account.enums.WalletAddressType;
import com.seeds.account.model.SystemWalletAddress;
import com.seeds.common.enums.Chain;

import java.util.List;

/**
 * <p>
 * 系统使用的钱包地址 服务类
 * </p>
 *
 * @author yk
 * @since 2022-09-28
 */
public interface ISystemWalletAddressService extends IService<SystemWalletAddress> {

    String getOne(Chain chain, WalletAddressType walletAddressType);

    /**
     * 从缓存获取
     *
     * @return
     */
    List<SystemWalletAddressDto> getAll();

    /**
     * 直接从数据库读取所有的合约配置
     *
     * @return
     */
    List<SystemWalletAddressDto> loadAll();

    List<String> getList(Chain chain, WalletAddressType walletAddressType);

    /**
     * 创建热钱包
     * @param systemWalletAddressDto
     */
    void add(SystemWalletAddressDto systemWalletAddressDto);

    /**
     * 更新系统热钱包
     * @param systemWalletAddressDto
     */
    void update(SystemWalletAddressDto systemWalletAddressDto);
}
