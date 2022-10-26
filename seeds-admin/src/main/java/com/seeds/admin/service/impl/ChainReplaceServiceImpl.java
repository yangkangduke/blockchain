package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.ChainTxnReplayDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.annotation.AuditLog;
import com.seeds.admin.enums.Action;
import com.seeds.admin.enums.Module;
import com.seeds.admin.enums.SubModule;
import com.seeds.admin.service.ChainReplaceService;
import com.seeds.common.dto.GenericDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ChainReplaceServiceImpl implements ChainReplaceService {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    @AuditLog(module = Module.REPLACE_MANAGEMENT, subModule = SubModule.CHAIN_REPLACE, action = Action.WITHDRAW)
    public GenericDto<Boolean> executeWithdraw(ChainTxnReplayDto dto) {
        return accountFeignClient.executeChainReplay(dto);
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.WALLET_ACCOUNT, action = Action.TRANSFER)
    public GenericDto<Boolean> executeWalletTransfer(ChainTxnReplayDto dto) {
        return accountFeignClient.executeChainReplay(dto);
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.ACCOUNT_TRANSFER, action = Action.ADD)
    public GenericDto<Boolean> executeCreateOrder(ChainTxnReplayDto dto) {
        return accountFeignClient.executeChainReplay(dto);
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.GAS_FEE_TRANSFER, action = Action.ADD)
    public GenericDto<Boolean> executeCreateGasFeeOrder(ChainTxnReplayDto dto) {
        return accountFeignClient.executeChainReplay(dto);
    }

    @Override
    public GenericDto<Page<ChainTxnDto>> getChainTxnList(ChainTxnPageReq req) {
        return accountFeignClient.getChainTxnList(req);
    }

}
