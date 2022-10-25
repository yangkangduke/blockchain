package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.account.dto.req.AccountHistoryReq;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.common.enums.Chain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 充提历史 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface ChainDepositWithdrawHisMapper extends BaseMapper<ChainDepositWithdrawHis> {

    /**
     * 获取用户当天的提币
     *
     * @param userId
     * @param currency
     * @param startTime
     * @return
     */
    List<ChainDepositWithdrawHis> getIntradayWithdraw(long userId, String currency, long startTime);

    List<ChainDepositWithdrawHis> getChainDepositWithdrawEarlierThanBlockNumber(Chain chain, int action, int status, long blockNumber);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatusByPrimaryKey(Long id, int status);

    /**
     * 回滚
     *
     * @param blockNumber
     * @param blockHash
     * @param status
     * @return
     */
    int rollbackByBlockNumberAndBlockHash(Chain chain, long blockNumber, String blockHash, int status);

    /**
     * 根据状态来查询
     *
     * @param action
     * @param statusList
     * @return
     */
    List<ChainDepositWithdrawHis> getListByStatus(int action, List<Integer> statusList);

    /**
     * 获取历史
     */
    IPage<ChainDepositWithdrawHisDto> selectByUserAndTime(Page page, @Param("query") AccountHistoryReq accountHistoryReq);

    /**
     * 获取需要审核的充提
     * @param page
     * @param transactionsReq
     * @return
     */
    Page<ChainDepositWithdrawHisDto> getDepositWithdrawList(Page page, @Param("query") AccountPendingTransactionsReq transactionsReq);
}
