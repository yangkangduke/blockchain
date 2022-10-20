package com.seeds.account.service;


import com.seeds.account.dto.UserAccountSummaryDto;
import com.seeds.account.dto.WithdrawRequestDto;
import com.seeds.account.dto.WithdrawResponseDto;

import java.math.BigDecimal;
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

    /**
     * MGT同意
     *
     * @param id
     * @param comments
     */
    void approveTransaction(long id, String comments);

    /**
     * MGT拒绝
     *
     * @param id
     * @param comments
     */
    void rejectTransaction(long id, String comments);

    /**
     * 获取用户钱包账户数据
     *
     * @param userId
     * @return
     */
    UserAccountSummaryDto getUserWalletAccountSummaryDto(long userId);

    /**
     * 获取用户钱包账户数据
     *
     * @param userId
     * @param priceMap
     * @return
     */
    UserAccountSummaryDto getUserWalletAccountSummaryDto(long userId, Map<String, BigDecimal> priceMap);

}
