package com.seeds.account.service.impl;

import com.seeds.account.enums.WalletAddressType;
import com.seeds.account.mapper.ChainDepositAddressMapper;
import com.seeds.account.mapper.SystemWalletAddressMapper;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.service.IChainDepositService;
import com.seeds.account.service.ISystemWalletAddressService;
import com.seeds.account.service.ILockService;
import com.seeds.account.util.Utils;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @program: seeds-java
 * @description: 充币
 * @author: yk
 * @create: 2022-09-28 10:56
 **/
@Slf4j
@Service
public class ChainDepositServiceImpl implements IChainDepositService {

    @Autowired
    ISystemWalletAddressService systemWalletAddressService;
    @Autowired
    SystemWalletAddressMapper systemWalletAddressMapper;
    @Autowired
    ChainDepositAddressMapper chainDepositAddressMapper;
    @Autowired
    ILockService lockService;


    @Override
    public String getDepositAddress(Chain chain, long userId, boolean createIfNull) throws Exception {
        log.info("getDepositAddress chain={} user={} createIfNull={}", chain, userId, createIfNull);

        if (createIfNull) {
            Assert.isTrue(userId >= 0, "invalid userId");
        }
        // 2021-04-27 milo 增加链校验，以防止用户用户的暴力访问
        Utils.check(chain != null, ErrorCode.ACCOUNT_INVALID_CHAIN);

        // 2021-04-27 milo 如果用户请求的是DEFI充提地址
        if (Chain.SUPPORT_DEFI_LIST.contains(chain)) {
            String defiDepositWithdrawAddress = systemWalletAddressService.getOne(chain.getRelayOn(), WalletAddressType.DEFI_DEPOSIT_WITHDRAW_CONTRACT);
            return defiDepositWithdrawAddress;
        }

        // 2021-04-27 milo 链做个映射，主要的目的是BSC的地址跟ETH的地址共享
        Chain mappedChain = Chain.mapChain(chain);
        // 查找分配地址
        ChainDepositAddress chainDepositAddress = chainDepositAddressMapper.getAssignedAddressByUserId(mappedChain.getCode(), userId);
        if (chainDepositAddress != null) {
            return chainDepositAddress.getAddress();
        }
        if (createIfNull) {
            return lockService.executeInLock("account:deposit-address:assign:lock", () -> {
                // 获取待分配列表
                List<ChainDepositAddress> idleAddresses = chainDepositAddressMapper.getIdleAddresses(mappedChain.getCode());
                for (ChainDepositAddress idleAddress : idleAddresses) {
                    // 尝试分配
                    int updated = chainDepositAddressMapper.assignAddress(mappedChain.getCode(), idleAddress.getAddress(), userId);
                    if (updated > 0) {
                        return idleAddress.getAddress();
                    }
                }
                return null;
            });
        }
        return null;
    }

    @Override
    public ChainDepositAddress getByAddress(Chain chain, String address) {
        // 2021-04-27 milo 增加链校验，以防止用户用户的暴力访问
        Utils.check(chain != null, ErrorCode.ACCOUNT_INVALID_CHAIN);

        // 2021-04-27 milo 链做个映射，主要的目的是BSC的地址跟ETH的地址共享
        chain = Chain.mapChain(chain);
        return chainDepositAddressMapper.getByAddress(chain.getCode(), address);
    }
}
