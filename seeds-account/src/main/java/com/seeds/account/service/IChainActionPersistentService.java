package com.seeds.account.service;

import com.seeds.account.model.ChainDepositAddress;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * 所有的方法都是事务的
 *
 * @author milo
 *
 */
public interface IChainActionPersistentService {

    /**
     * 插入空闲地址
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    void insert(List<ChainDepositAddress> list);

}
