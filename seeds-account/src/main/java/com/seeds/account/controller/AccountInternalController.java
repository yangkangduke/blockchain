package com.seeds.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.seeds.account.dto.*;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.enums.DepositStatus;
import com.seeds.account.enums.WithdrawStatus;
import com.seeds.account.service.IAccountService;
import com.seeds.account.service.IAddressCollectService;
import com.seeds.account.service.IChainActionService;
import com.seeds.account.service.IChainDepositWithdrawHisService;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 账户系统提供的内部调用接口，调用方包括
 * 1. seeds-account-job-service
 *
 * @author yk
 */
@RestController
@Slf4j
@Api(tags = "账户-内部调用")
@RequestMapping("/account-internal")
public class AccountInternalController {

    @Autowired
    private IChainActionService chainActionService;
    @Autowired
    private IAddressCollectService addressCollectService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IChainDepositWithdrawHisService chainDepositWithdrawHisService;

    @PostMapping("/job/scan-and-create-addresses")
    @ApiOperation("扫描并创建空闲地址")
    @Inner
    public GenericDto<Boolean> scanAndCreateAddresses() {
        try {
            chainActionService.scanAndCreateAddresses();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("scanAndCreateAddresses", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/job/scan-block")
    @ApiOperation("扫描新块")
    @Inner
    public GenericDto<Boolean> scanBlock() {
        try {
            chainActionService.scanBlock();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("scanBlock", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取需要审核的充提
     *
     * @return
     */
    @PostMapping("/mgt/pending-transaction")
    @ApiOperation("获取需要审核的充提")
    @Inner
    public GenericDto<Page<ChainDepositWithdrawHisDto>> getPendingTransactions(@RequestBody AccountPendingTransactionsReq transactionsReq) {
        try {
            // 待审核状态的提币充币一样的
            List<Integer> statusList = Lists.newArrayList(DepositStatus.PENDING_APPROVE.getCode());
            transactionsReq.setStatusList(statusList);
            Page page = new Page();
            page.setCurrent(transactionsReq.getCurrent());
            page.setSize(transactionsReq.getSize());
            transactionsReq.setOnlyManualCheck(true);
            transactionsReq.setStartTime(0L);
            transactionsReq.setEndTime(System.currentTimeMillis());
            Page<ChainDepositWithdrawHisDto> list = chainDepositWithdrawHisService.getDepositWithdrawList(page, transactionsReq);
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getPendingTransactions", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 充币提币审核通过
     *
     * @param approveRejectDto
     * @return
     */
    @PostMapping("/sys/approve-transaction")
    @ApiOperation("充币提币审核通过")
    @Inner
    public GenericDto<Boolean> approveTransaction(@RequestBody ApproveRejectDto approveRejectDto) {
        try {
            accountService.approveTransaction(approveRejectDto.getId(), approveRejectDto.getComment());
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("approveWithdraw", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 充币提币审核拒绝
     *
     * @param approveRejectDto
     * @return
     */
    @PostMapping("/sys/reject-transaction")
    @ApiOperation("充币提币审核拒绝")
    @Inner
    public GenericDto<Boolean> rejectTransaction(@RequestBody ApproveRejectDto approveRejectDto) {
        try {
            accountService.rejectTransaction(approveRejectDto.getId(), approveRejectDto.getComment());
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("rejectWithdraw", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取审核的充提
     *
     * @return
     */
    @PostMapping("/sys/processed-transaction")
    @ApiOperation("获取审核的充提")
    public GenericDto<Page<ChainDepositWithdrawHisDto>> getManualProcessedTransactions(@RequestBody AccountPendingTransactionsReq transactionsReq) {
        try {
            Integer status = transactionsReq.getStatus();
            List<Integer> statusList = status == null
                    ? Lists.newArrayList(DepositStatus.APPROVED.getCode(), DepositStatus.REJECTED.getCode(), DepositStatus.TRANSACTION_CONFIRMED.getCode(),
                    WithdrawStatus.TRANSACTION_CONFIRMED.getCode(), WithdrawStatus.TRANSACTION_FAILED.getCode())
                    : Lists.newArrayList(status);

            transactionsReq.setStatusList(statusList);
            Page page = new Page();
            page.setCurrent(transactionsReq.getCurrent());
            page.setSize(transactionsReq.getSize());
            transactionsReq.setOnlyManualCheck(true);
            transactionsReq.setStartTime(0L);
            transactionsReq.setEndTime(System.currentTimeMillis());
            Page<ChainDepositWithdrawHisDto> list = chainDepositWithdrawHisService.getDepositWithdrawList(page, transactionsReq);
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getPendingTransactions", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/job/execute-withdraw")
    @ApiOperation("执行提币上链")
    @Inner
    public GenericDto<Boolean> executeWithdraw() {
        try {
            chainActionService.executeWithdraw();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("executeWithdraw", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/job/scan-withdraw")
//    @ApiOperation("扫描提币，归集，空投状态")
    @ApiOperation("扫描提币状态")
    @Inner
    public GenericDto<Boolean> scanWithdraw() {
        try {
            // 处理提币
            chainActionService.scanWithdraw();
            // 处理归集
//            addressCollectService.scanCollect();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("scanWithdraw", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取链上原始交易list
     *
     * @return
     */
    @PostMapping("/sys/chain/transaction")
    @ApiOperation("获取链上原始交易list")
    @Inner
    public GenericDto<IPage<ChainTxnDto>> getChainTxnList(@RequestBody @Valid ChainTxnPageReq req) {
        try {
            return GenericDto.success(chainActionService.getTxnList(req));
        } catch (Exception e) {
            log.error("getChainTxnList", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/chain/replacement")
    @ApiOperation("获取链上替换交易list")
    @Inner
    public GenericDto<IPage<ChainTxnDto>> getChainTxnReplaceList(@RequestBody @Valid ChainTxnPageReq req) {
        try {
            return GenericDto.success(chainActionService.getTxnReplaceList(req));
        } catch (Exception e) {
            log.error("getChainTxnReplaceList", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/chain/replay/execute")
    @ApiOperation("执行链上 tx 重发")
    @Inner
    public GenericDto<Boolean> executeChainReplay(@RequestBody @Valid ChainTxnReplayDto chainTxnReplayDto) {
        try {
            return GenericDto.success(chainActionService.replayTransaction(chainTxnReplayDto));
        } catch (Exception e) {
            log.error("executeChainReplay", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/chain/replacement/execute")
    @ApiOperation("执行链上 tx 替换")
    @Inner
    public GenericDto<Long> executeChainReplacement(@RequestBody @Valid ChainTxnReplaceDto chainTxnReplaceDto) {
        try {
            return GenericDto.success(chainActionService.replaceTransaction(chainTxnReplaceDto));
        } catch (Exception e) {
            log.error("executeChainReplacement", e);
            return Utils.returnFromException(e);
        }
    }

}
