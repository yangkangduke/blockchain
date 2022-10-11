package com.seeds.account.service;


import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import com.seeds.account.model.UserAccountActionHis;

import java.math.BigDecimal;
import java.util.List;

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
}
