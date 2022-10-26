package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.ChainTxnReplayDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.annotation.AuditLog;
import com.seeds.admin.dto.MgtChainTxnReplaceDto;
import com.seeds.admin.dto.MgtChainTxnReplayDto;
import com.seeds.admin.dto.MgtOriginOrderReplaceDto;
import com.seeds.admin.enums.Action;
import com.seeds.admin.enums.Module;
import com.seeds.admin.enums.SubModule;
import com.seeds.admin.service.ChainReplaceService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.dto.PagedDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@Slf4j
@Service
public class ChainReplaceServiceImpl implements ChainReplaceService {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    @AuditLog(module = Module.REPLACE_MANAGEMENT, subModule = SubModule.CHAIN_REPLACE, action = Action.WITHDRAW)
    public GenericDto<Boolean> executeWithdraw(MgtChainTxnReplayDto chainTxnReplayDto) {
        return executeChainReplay(chainTxnReplayDto);
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.WALLET_ACCOUNT, action = Action.TRANSFER)
    public GenericDto<Boolean> executeWalletTransfer(MgtChainTxnReplayDto chainTxnReplayDto) {
        return executeChainReplay(chainTxnReplayDto);
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.ACCOUNT_TRANSFER, action = Action.ADD)
    public GenericDto<Boolean> executeCreateOrder(MgtChainTxnReplayDto chainTxnReplayDto) {
        return executeChainReplay(chainTxnReplayDto);
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.GAS_FEE_TRANSFER, action = Action.ADD)
    public GenericDto<Boolean> executeCreateGasFeeOrder(MgtChainTxnReplayDto chainTxnReplayDto) {
        return executeChainReplay(chainTxnReplayDto);
    }

    @Override
    public GenericDto<Page<ChainTxnDto>> getChainTxnList(ChainTxnPageReq req) {
        GenericDto<Page<ChainTxnDto>> chainTxnList = accountFeignClient.getChainTxnList(req);
        if (!chainTxnList.isSuccess()) {
            return GenericDto.failure(chainTxnList.getMessage(), chainTxnList.getCode());
        }
        return chainTxnList;
    }

    @Override
    @AuditLog(module = Module.REPLACE_MANAGEMENT, subModule = SubModule.CHAIN_REPLACE, action = Action.REPLACED)
    public void recordOriginReplaceOrder(MgtOriginOrderReplaceDto dto) {

    }

    @Override
    @AuditLog(module = Module.REPLACE_MANAGEMENT, subModule = SubModule.CHAIN_REPLACE, action = Action.REPLACE)
    public GenericDto<Boolean> executeChainReplacement(MgtChainTxnReplaceDto mgtChainTxnReplaceDto) {
        GenericDto<Long> dto = accountFeignClient.executeChainReplacement(mgtChainTxnReplaceDto);
        if (!dto.isSuccess()) {
            return GenericDto.failure(dto.getMessage(), dto.getCode());
        }
        mgtChainTxnReplaceDto.setId(dto.getData());
        return GenericDto.success(true);
    }

    @Override
    public GenericDto<Page<ChainTxnDto>> getChainTxnReplaceList(ChainTxnPageReq req) {
        GenericDto<Page<ChainTxnDto>> chainTxnReplaceList = accountFeignClient.getChainTxnReplaceList(req);
        if (!chainTxnReplaceList.isSuccess()) {
            return GenericDto.failure(chainTxnReplaceList.getMessage(),
                    chainTxnReplaceList.getCode());
        }
        return chainTxnReplaceList;
    }

    private GenericDto<Boolean> executeChainReplay(@RequestBody @Valid MgtChainTxnReplayDto chainTxnReplayDto) {
        return accountFeignClient.executeChainReplay(ChainTxnReplayDto.builder()
                .id(chainTxnReplayDto.getId())
                .type(chainTxnReplayDto.getType())
                .txHash(chainTxnReplayDto.getTxHash())
                .build());
    }

}
