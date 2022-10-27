package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.AccountConstants;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.account.dto.req.AccountHistoryReq;
import com.seeds.account.enums.ChainAction;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.mapper.ChainDepositWithdrawHisMapper;
import com.seeds.account.mapper.ChainDepositWithdrawSigHisMapper;
import com.seeds.account.mapper.UserAccountActionHisMapper;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.account.model.ChainDepositWithdrawSigHis;
import com.seeds.account.service.IChainDepositWithdrawHisService;
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
    private IChainService chainService;
    @Autowired
    ChainDepositWithdrawHisMapper chainDepositWithdrawHisMapper;
    @Autowired
    ChainDepositWithdrawSigHisMapper chainDepositWithdrawSigHisMapper;
    @Autowired
    UserAccountActionHisMapper userAccountActionHisMapper;


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
    public IPage<ChainDepositWithdrawHisDto> getHistory(Page page, AccountHistoryReq accountHistoryReq) {
        return chainDepositWithdrawHisMapper.selectByUserAndTime(page, accountHistoryReq);
    }

    @Override
    public Page<ChainDepositWithdrawHisDto> getDepositWithdrawList(Page page, AccountPendingTransactionsReq transactionsReq) {
        return baseMapper.getDepositWithdrawList(page, transactionsReq);
    }

    @Override
    public IPage<ChainDepositWithdrawHis> selectByChainStatusAndTimestamp(Page page, Chain chain, List<Integer> asList, ChainAction withdraw, long expireTimestamp) {
        LambdaQueryWrapper<ChainDepositWithdrawHis> queryWrap = new QueryWrapper<ChainDepositWithdrawHis>().lambda()
                .eq(chain.getCode() != null, ChainDepositWithdrawHis::getChain, chain.getCode())
                .eq(ChainDepositWithdrawHis::getAction, withdraw.getCode())
                .in(ChainDepositWithdrawHis::getStatus, asList)
                .le(ChainDepositWithdrawHis::getUpdateTime, expireTimestamp)
                .orderByDesc(ChainDepositWithdrawHis::getId);
        Page<ChainDepositWithdrawHis> newPage = new Page<>(page.getCurrent(), page.getSize());
        return page(newPage, queryWrap);
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

    @Override
    public List<String> getDepositAddress(Chain chain, long startTime, long endTime) {
        return chainDepositWithdrawHisMapper.getDepositAddress(chain, startTime, endTime);
    }

    private BigInteger getChainGasPrice(Chain chain) {
        return BigInteger.valueOf(chainService.getGasPrice(chain));
    }

    private ChainTxnDto convert2Dto(ChainDepositWithdrawHis e, BigInteger chainGasPrice, int type) {
        ChainTxnDto obj = ObjectUtils.copy(e, new ChainTxnDto());
        obj.setGasPrice(BigInteger.valueOf(e.getGasPrice()));
        obj.setGasLimit(BigInteger.valueOf(e.getGasLimit()));
        obj.setType(type);
        obj.setNonce(new BigInteger(e.getNonce()));
        obj.setChainGasPrice(chainGasPrice);
        obj.setChain(e.getChain().getCode());
        try {
            obj.setConfirmedSafeNonce(chainService.getSafeConfirmedNonce(e.getChain(), e.getFromAddress()));
        } catch (IOException ioException) {
            log.error("error, ", ioException);
        }
        return obj;
    }
}
