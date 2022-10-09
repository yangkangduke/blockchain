package com.seeds.account.service.impl;

import com.seeds.account.mapper.ChainDepositAddressMapper;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.service.IChainActionPersistentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * 关注事务操作
 *
 * @author milo
 *
 */
@Slf4j
@Service
public class ChainActionPersistentServiceImpl implements IChainActionPersistentService {

    @Autowired
    ChainDepositAddressMapper chainDepositAddressMapper;

    @Override
    public void insert(List<ChainDepositAddress> list) {
        list.forEach(e -> {
            log.info("insert new address={}", e);
            chainDepositAddressMapper.insert(e);
        });
    }
}
