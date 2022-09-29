package com.seeds.account.service.impl;

import com.seeds.account.AccountConstants;
import com.seeds.account.anno.SingletonLock;
import com.seeds.account.chain.service.ChainService;
import com.seeds.account.enums.AccountActionControl;
import com.seeds.account.enums.AccountSystemConfig;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.mapper.ChainDepositAddressMapper;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.service.ActionControlService;
import com.seeds.account.service.IChainActionService;
import com.seeds.account.service.SystemConfigService;
import com.seeds.common.enums.Chain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: seeds-java
 * @description: chain action
 * @author: yk
 * @create: 2022-09-28 17:36
 **/
@Slf4j
@Service
public class ChainActionServiceImpl implements IChainActionService {

    @Autowired
    private ActionControlService actionControlService;
    @Autowired
    private ChainDepositAddressMapper chainDepositAddressMapper;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    private ChainService chainService;

    @Override
    @SingletonLock(key = "/seeds/account/chain/scan-idle-addresses")
    public void scanAndCreateAddresses() throws Exception {
        boolean enabled = actionControlService.isEnabled(AccountActionControl.CREATE_CHAIN_ADDRESS);
        if (!enabled) {
            return;
        }
        // 2021-04-27 Milo 扫描需要创建地址的链 (遇到BSC链就映射成ETH链)
        for (Chain chain : Chain.SUPPORT_CREATE_ADDRESS_LIST) {
            chain = Chain.mapChain(chain);
            int chainCode = chain.getCode();

            int idles = chainDepositAddressMapper.countIdleAddresses(chainCode);
            int minIdles = Integer.parseInt(systemConfigService.getValue(AccountSystemConfig.CHAIN_MIN_IDLE_ADDRESS, "10"));
            if (idles >= minIdles) {
                continue;
            }
            int slot = minIdles - idles;
            if (slot <= 0) {
                continue;
            }
            try {
                log.info("checkAndCreateAddresses chain={} minIdles={} idles={} slot={}", chain, minIdles, idles, slot);
                List<String> addresses = chainService.createAddresses(chain, slot);
                if (addresses.size() > 0) {
                    List<ChainDepositAddress> list = addresses.stream()
                            .map(address -> ChainDepositAddress.builder()
                                    .createTime(System.currentTimeMillis())
                                    .updateTime(System.currentTimeMillis())
                                    .version(AccountConstants.DEFAULT_VERSION)
                                    .chain(String.valueOf(chainCode))
                                    .address(address)
                                    .userId(AccountConstants.NOBODY_USER_ID)
                                    .status(CommonStatus.ENABLED.getCode())
                                    .build()
                            ).collect(Collectors.toList());
                    // 交给一个事务service
//                    chainActionPersistentService.insert(list);
                }
            } catch (Exception e) {
                log.error("checkAndCreateAddresses chain={} minIdles={} idles={} slot={}", chain, minIdles, idles, slot, e);
            }
        }
    }
}
