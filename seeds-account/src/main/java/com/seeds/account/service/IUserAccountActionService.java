package com.seeds.account.service;


import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;

import java.math.BigDecimal;

/**
 * @author guocheng
 * @date 2020/12/24
 */
public interface IUserAccountActionService {

    /**
     * 创建历史
     *
     * @param userId
     * @param currency
     * @param action
     * @param amount
     * @param status
     */
    void createHistory(long userId, String currency, AccountAction action, BigDecimal amount, CommonActionStatus status);

    /**
     * 创建历史
     *
     * @param userId
     * @param currency
     * @param action
     * @param amount
     */
    void createHistory(long userId, String currency, AccountAction action, BigDecimal amount);

    /**
     * 创建历史
     *
     * @param userId
     * @param currency
     * @param action
     * @param source
     * @param amount
     * @param status
     */
    void createHistory(long userId, String currency, AccountAction action, String source, BigDecimal amount, CommonActionStatus status);

    /**
     * 更新状态
     *
     * @param action
     * @param userId
     * @param source
     * @param status
     * @return
     */
    int updateStatusByActionUserIdSource(AccountAction action, long userId, String source, CommonActionStatus status);}
