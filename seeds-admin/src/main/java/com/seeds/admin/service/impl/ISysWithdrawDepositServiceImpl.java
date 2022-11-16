package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ApproveRejectDto;
import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.enums.ChainAction;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.annotation.AuditLog;
import com.seeds.admin.dto.MgtApproveRejectDto;
import com.seeds.admin.dto.MgtDepositWithdrawDto;
import com.seeds.admin.enums.Action;
import com.seeds.admin.enums.Module;
import com.seeds.admin.enums.SubModule;
import com.seeds.admin.mapstruct.DepositWithdrawDtoMapper;
import com.seeds.admin.service.ISysWithdrawDepositService;
import com.seeds.common.dto.GenericDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ISysWithdrawDepositServiceImpl implements ISysWithdrawDepositService {

    @Autowired
    private AccountFeignClient accountFeignClient;
    @Autowired
    private DepositWithdrawDtoMapper depositWithdrawDtoMapper;

    @Override
    public Page<MgtDepositWithdrawDto> listPendingWithdraw(String currency, Long userId, Integer page, Integer size) {
        AccountPendingTransactionsReq transactionsReq = new AccountPendingTransactionsReq();
        transactionsReq.setAction(ChainAction.WITHDRAW.getCode());
        transactionsReq.setCurrency(currency);
        transactionsReq.setUserId(userId);
        transactionsReq.setCurrent(page);
        transactionsReq.setSize(size);
        GenericDto<Page<ChainDepositWithdrawHisDto>> dto = accountFeignClient.getPendingTransactions(transactionsReq);
        if (!dto.isSuccess()) {
            return new Page<>();
        }
        Page<MgtDepositWithdrawDto> pageList = new Page<>();
        List<MgtDepositWithdrawDto> mgtDepositWithdrawDtos = depositWithdrawDtoMapper.convert2MgtDepositWithdrawDtos(dto.getData().getRecords());
        pageList.setRecords(mgtDepositWithdrawDtos);
        pageList.setCurrent(dto.getData().getCurrent());
        pageList.setSize(dto.getData().getSize());
        pageList.setPages(dto.getData().getPages());
        pageList.setTotal(dto.getData().getTotal());
        return pageList;
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.WITHDRAW_REVIEW, action = Action.APPROVE)
    public GenericDto<Boolean> approvePendingWithdraw(MgtApproveRejectDto dto) {
        return accountFeignClient.approveTransaction(ApproveRejectDto.builder()
                .id(dto.getId())
                .comment(dto.getComment())
                .build());
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.WITHDRAW_REVIEW, action = Action.REJECT)
    public GenericDto<Boolean> rejectPendingWithdraw(MgtApproveRejectDto dto) {
        return accountFeignClient.rejectTransaction(ApproveRejectDto.builder()
                .id(dto.getId())
                .comment(dto.getComment())
                .build());
    }

    @Override
    public Page<MgtDepositWithdrawDto> listWithdrawReviewHis(String currency, Long userId, Integer status, Integer page, Integer size) {
        AccountPendingTransactionsReq transactionsReq = new AccountPendingTransactionsReq();
        transactionsReq.setAction(ChainAction.WITHDRAW.getCode());
        transactionsReq.setCurrency(currency);
        transactionsReq.setUserId(userId);
        transactionsReq.setCurrent(page);
        transactionsReq.setSize(size);
        GenericDto<Page<ChainDepositWithdrawHisDto>> dto = accountFeignClient.getManualProcessedTransactions(transactionsReq);
        if (!dto.isSuccess()) {
            return new Page<>();
        }
        Page<MgtDepositWithdrawDto> pageList = new Page<>();
        List<MgtDepositWithdrawDto> mgtDepositWithdrawDtos = depositWithdrawDtoMapper.convert2MgtDepositWithdrawDtos(dto.getData().getRecords());
        pageList.setRecords(mgtDepositWithdrawDtos);
        pageList.setCurrent(dto.getData().getCurrent());
        pageList.setSize(dto.getData().getSize());
        pageList.setPages(dto.getData().getPages());
        pageList.setTotal(dto.getData().getTotal());
        return pageList;
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.DEPOSIT_REVIEW, action = Action.APPROVE)
    public GenericDto<Boolean> approvePendingDeposit(MgtApproveRejectDto dto) {
        return accountFeignClient.approveTransaction(ApproveRejectDto.builder()
                .id(dto.getId())
                .comment(dto.getComment())
                .build());
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.DEPOSIT_REVIEW, action = Action.REJECT)
    public GenericDto<Boolean> rejectPendingDeposit(MgtApproveRejectDto dto) {
        return accountFeignClient.rejectTransaction(ApproveRejectDto.builder()
                .id(dto.getId())
                .comment(dto.getComment())
                .build());
    }

    @Override
    public Page<MgtDepositWithdrawDto> listDepositReviewHis(String currency, Long userId, Integer status, Integer page, Integer size) {
        AccountPendingTransactionsReq transactionsReq = new AccountPendingTransactionsReq();
        transactionsReq.setAction(ChainAction.DEPOSIT.getCode());
        transactionsReq.setCurrency(currency);
        transactionsReq.setUserId(userId);
        transactionsReq.setCurrent(page);
        transactionsReq.setSize(size);
        GenericDto<Page<ChainDepositWithdrawHisDto>> dto = accountFeignClient.getManualProcessedTransactions(transactionsReq);
        if (!dto.isSuccess()) {
            return new Page<>();
        }
        Page<MgtDepositWithdrawDto> pageList = new Page<>();
        List<MgtDepositWithdrawDto> mgtDepositWithdrawDtos = depositWithdrawDtoMapper.convert2MgtDepositWithdrawDtos(dto.getData().getRecords());
        pageList.setRecords(mgtDepositWithdrawDtos);
        pageList.setCurrent(dto.getData().getCurrent());
        pageList.setSize(dto.getData().getSize());
        pageList.setPages(dto.getData().getPages());
        pageList.setTotal(dto.getData().getTotal());
        return pageList;
    }

    @Override
    public Page<MgtDepositWithdrawDto> listPendingDeposit(String currency, Long userId, Integer page, Integer size) {
        AccountPendingTransactionsReq transactionsReq = new AccountPendingTransactionsReq();
        transactionsReq.setAction(ChainAction.DEPOSIT.getCode());
        transactionsReq.setCurrency(currency);
        transactionsReq.setUserId(userId);
        transactionsReq.setCurrent(page);
        transactionsReq.setSize(size);
        GenericDto<Page<ChainDepositWithdrawHisDto>> dto = accountFeignClient.getPendingTransactions(transactionsReq);
        if (!dto.isSuccess()) {
            return new Page<>();
        }
        Page<MgtDepositWithdrawDto> pageList = new Page<>();
        List<MgtDepositWithdrawDto> mgtDepositWithdrawDtos = depositWithdrawDtoMapper.convert2MgtDepositWithdrawDtos(dto.getData().getRecords());
        pageList.setRecords(mgtDepositWithdrawDtos);
        pageList.setCurrent(dto.getData().getCurrent());
        pageList.setSize(dto.getData().getSize());
        pageList.setPages(dto.getData().getPages());
        pageList.setTotal(dto.getData().getTotal());
        return pageList;
    }
}
