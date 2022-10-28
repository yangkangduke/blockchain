package com.seeds.account.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.account.dto.req.AccountHistoryReq;
import com.seeds.account.enums.ChainAction;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.account.model.ChainDepositWithdrawSigHis;
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

    /**
     * 获取签名历史，需要传入userId校验，以免用户尝试id
     * @param id
     * @param userId
     * @return
     */
    ChainDepositWithdrawSigHis getSigHistory(long id, long userId);

    /**
     * 更新充提币历史
     * @param chainDepositWithdrawHis
     */
    void updateHistory(ChainDepositWithdrawHis chainDepositWithdrawHis);

    /**
     * 查询充提币历史
     * @param id
     * @return
     */
    ChainDepositWithdrawHis getHistory(long id);

    /**
     * 查询某一个人的充提记录
     *
     * @return
     */
    IPage<ChainDepositWithdrawHisDto> getHistory(Page page, AccountHistoryReq accountHistoryReq);


    IPage<ChainDepositWithdrawHis> selectByChainStatusAndTimestamp(Page page, Chain chain, List<Integer> asList, ChainAction withdraw, long expireTimestamp);


    /**
     * 获取需要审核的充提
     * @param page
     * @param transactionsReq
     * @return
     */
    Page<ChainDepositWithdrawHisDto> getDepositWithdrawList(Page page, AccountPendingTransactionsReq transactionsReq);

    /**
     * 获取一段时间内充币的地址
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<String> getDepositAddress(Chain chain, long startTime, long endTime);

}
