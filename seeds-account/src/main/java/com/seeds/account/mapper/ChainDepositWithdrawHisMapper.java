package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.model.ChainDepositWithdrawHis;

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
}
