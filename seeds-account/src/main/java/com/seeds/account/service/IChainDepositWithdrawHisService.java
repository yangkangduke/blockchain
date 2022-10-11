package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.common.enums.Chain;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 充提历史 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface IChainDepositWithdrawHisService extends IService<ChainDepositWithdrawHis> {

    /**
     * 查询日内提币
     * @param userId
     * @param currency
     * @param startTime
     * @return
     */
    List<ChainDepositWithdrawHis> getIntradayWithdraw(long userId, String currency, long startTime);

    /**
     *
     * @param chainDepositWithdrawHis
     * @return
     */
    void createHistory(ChainDepositWithdrawHis chainDepositWithdrawHis);

    void createSigHistory(long id, long userId, Chain chain, String currency, BigDecimal amount, String signedSignature, String signedMessage, long deadline);

}
