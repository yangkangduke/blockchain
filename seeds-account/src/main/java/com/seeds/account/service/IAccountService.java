package com.seeds.account.service;


import com.seeds.account.dto.WithdrawRequestDto;
import com.seeds.account.dto.WithdrawResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author yk
 */
public interface IAccountService {

    /**
     * 用户提币
     *
     * @param userId
     * @param withdrawRequestDto
     * @throws
     */
    WithdrawResponseDto withdraw(long userId, WithdrawRequestDto withdrawRequestDto) throws Exception;

    void checkFundingStatus();

    /**
     * 获取当日已经使用额度
     * @param userId
     * @param currency
     * @return
     */
    BigDecimal getUsedIntradayWithdraw(long userId, String currency);
}
