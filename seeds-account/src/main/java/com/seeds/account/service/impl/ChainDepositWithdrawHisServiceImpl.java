package com.seeds.account.service.impl;

import com.seeds.account.AccountConstants;
import com.seeds.account.mapper.ChainDepositWithdrawHisMapper;
import com.seeds.account.mapper.ChainDepositWithdrawSigHisMapper;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.account.model.ChainDepositWithdrawSigHis;
import com.seeds.account.service.IChainDepositWithdrawHisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.common.enums.Chain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 充提历史 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Service
@Slf4j
public class ChainDepositWithdrawHisServiceImpl extends ServiceImpl<ChainDepositWithdrawHisMapper, ChainDepositWithdrawHis> implements IChainDepositWithdrawHisService {

    @Autowired
    ChainDepositWithdrawHisMapper chainDepositWithdrawHisMapper;
    @Autowired
    ChainDepositWithdrawSigHisMapper chainDepositWithdrawSigHisMapper;


    @Override
    public List<ChainDepositWithdrawHis> getIntradayWithdraw(long userId, String currency, long startTime) {
        return chainDepositWithdrawHisMapper.getIntradayWithdraw(userId, currency, startTime);
    }

    @Override
    public void createHistory(ChainDepositWithdrawHis chainDepositWithdrawHis) {
        chainDepositWithdrawHisMapper.insert(chainDepositWithdrawHis);
        log.info("createHistory chainDepositWithdrawHis={}", chainDepositWithdrawHis);
    }

    @Override
    public ChainDepositWithdrawSigHis getSigHistory(long id, long userId) {
        return chainDepositWithdrawSigHisMapper.getByUserId(id, userId);
    }

    @Override
    public void updateHistory(ChainDepositWithdrawHis chainDepositWithdrawHis) {
        chainDepositWithdrawHisMapper.updateById(chainDepositWithdrawHis);
    }

    @Override
    public ChainDepositWithdrawHis getHistory(long id) {
        return chainDepositWithdrawHisMapper.selectById(id);
    }

    @Override
    @Async("executorPool")
    public void createSigHistory(long id, long userId, Chain chain, String currency, BigDecimal amount, String signedSignature, String signedMessage, long deadline) {
        ChainDepositWithdrawSigHis chainDepositWithdrawSigHis = ChainDepositWithdrawSigHis.builder()
                .id(id)
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .version(AccountConstants.DEFAULT_VERSION)
                .userId(userId)
                .chain(chain)
                .currency(currency)
                .amount(amount)
                .deadline(deadline)
                .signedSignature(signedSignature)
                .signedMessage(signedMessage)
                .build();
        log.info("createSigHistory chainDepositWithdrawSigHis={}", chainDepositWithdrawSigHis);
        chainDepositWithdrawSigHisMapper.insert(chainDepositWithdrawSigHis);
    }
}
