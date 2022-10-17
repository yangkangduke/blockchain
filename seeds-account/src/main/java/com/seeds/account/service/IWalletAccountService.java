package com.seeds.account.service;

import com.seeds.account.model.UserAccount;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yk
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

    /**
     * 更新余额
     *
     * @param userId
     * @param currency
     * @param amount   !=0
     * @param geZero   结果是否满足>=0
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateAvailable(Long userId, String currency, BigDecimal amount, boolean geZero);

    /**
     * 更新冻结
     *
     * @param userId
     * @param currency
     * @param amount   !=0
     * @param geZero   结果是否满足>=0
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateFreeze(Long userId, String currency, BigDecimal amount, boolean geZero);

    /**
     * 解冻
     *
     * @param userId
     * @param currency
     * @param amount   > 0
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean unfreeze(Long userId, String currency, BigDecimal amount);

    /**
     * 兑换时，一个事物中 更新用户冻结 & 可用
     *
     * @param userId         用户userId
     * @param sourceCurrency 兑换源币种
     * @param sourceAmount   兑换源数额
     * @param targetCurrency 兑换目的币种
     * @param targetAmount   兑换目的数额
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateFreezeAndAvailable(Long userId, String sourceCurrency, BigDecimal sourceAmount, String targetCurrency, BigDecimal targetAmount);

    /**
     * 账户列表
     *
     * @param userId
     * @return
     */
    List<UserAccount> getAccounts(long userId);


}
