package com.seeds.account.service;


import com.seeds.account.dto.SystemWalletAddressDto;
import com.seeds.common.enums.Chain;

import java.util.List;

/**
 *
 * @author yk
 *
 */
public interface IChainDepositService {

    /**
     * 获取充币地址
     *
     * @param chain
     * @param userId
     * @param createIfNull
     * @return
     */
    String getDepositAddress(Chain chain, long userId, boolean createIfNull) throws Exception;

    /**
     * 直接从数据库读取所有的合约配置
     *
     * @return
     */
    List<SystemWalletAddressDto> loadAll();

}
