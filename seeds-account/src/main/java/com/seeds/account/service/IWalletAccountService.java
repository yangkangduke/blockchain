package com.seeds.account.service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author milo
 */
public interface IWalletAccountService {

    /**
     * 冻结
     *
     * @param userId
     * @param currency
     * @param amount   > 0
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean freeze(Long userId, String currency, BigDecimal amount);
}
