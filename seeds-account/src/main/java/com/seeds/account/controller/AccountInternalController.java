package com.seeds.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.account.dto.ApproveRejectDto;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.ChainTxnReplayDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.service.IAccountService;
import com.seeds.account.service.IAddressCollectService;
import com.seeds.account.service.IChainActionService;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
     * 充币提币审核通过
     *
     * @param approveRejectDto
     * @return
     */
    @PostMapping("/mgt/approve-transaction")
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
    @PostMapping("/mgt/reject-transaction")
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
    @PostMapping("/mgt/chain/transaction")
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

    @PostMapping("/mgt/chain/replay/execute")
    @ApiOperation("执行链上 tx replace")
    @Inner
    public GenericDto<Boolean> executeChainReplay(@RequestBody @Valid ChainTxnReplayDto chainTxnReplayDto) {
        try {
            return GenericDto.success(chainActionService.replayTransaction(chainTxnReplayDto));
        } catch (Exception e) {
            log.error("executeChainReplay", e);
            return Utils.returnFromException(e);
        }
    }
}
