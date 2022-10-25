package com.seeds.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.seeds.account.dto.ApproveRejectDto;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.ChainTxnReplayDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.enums.DepositStatus;
import com.seeds.account.enums.WithdrawStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.*;
import com.seeds.account.service.IAccountService;
import com.seeds.account.service.IAddressCollectHisService;
import com.seeds.account.service.IAddressCollectService;
import com.seeds.account.service.IChainActionService;
import com.seeds.account.service.IChainDepositWithdrawHisService;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    IAddressCollectHisService addressCollectHisService;
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
            addressCollectService.scanCollect();
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

    @PostMapping("/sys/chain/replay/execute")
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
//    @PostMapping("/mgt/chain/replay/execute")
//    @ApiOperation("执行链上 tx replace")
//    public GenericDto<Boolean> executeChainReplay(@RequestBody @Valid ChainTxnReplayDto chainTxnReplayDto) {
//        try {
//            return GenericDto.success(chainTxnReplaceService.replayTransaction(chainTxnReplayDto));
//        } catch (Exception e) {
//            log.error("executeChainReplay", e);
//            return Utils.returnFromException(e);
//        }
//    }


//    @PostMapping("/job/fund-collect-scan-pending-balances")
//    @ApiOperation("定期收集待归集地址余额")
//    public GenericDto<Boolean> scanPendingCollectBalances() {
//        try {
//            addressCollectService.scanPendingCollectBalances();
//            return GenericDto.success(true);
//        } catch (Exception e) {
//            log.error("scanPendingCollectBalances", e);
//            return Utils.returnFromException(e);
//        }
//    }

    /**
     * 获取待归集余额
     *
     * @return
     */
    @GetMapping("/mgt/fund-collect-scan-pending-balances")
    @ApiOperation("获取待归集余额")
    public GenericDto<Map<Chain, Map<String, BigDecimal>>> getPendingCollectBalances() {
        return GenericDto.success(addressCollectService.getPendingCollectBalances());
    }

    /**
     * 创建获取用户充币地址余额的请求
     * @param chain
     * @return
     */
    @PostMapping("/mgt/create-balances-get")
    @ApiOperation("创建获取用户充币地址余额的请求")
    public GenericDto<Boolean> createBalanceGet(@RequestParam("chain") int chain) {
        try {
            addressCollectService.createBalanceGet(Chain.fromCode(chain));
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("createBalanceGet", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 查询获取用户充币地址余额的请求的状态
     * @param chain
     * @return
     */
    @GetMapping("/mgt/get-balances-get-status")
    @ApiOperation("查询获取用户充币地址余额的请求的状态")
    public GenericDto<BalanceGetStatusDto> getBalanceGetStatus(@RequestParam(value = "chain") int chain) {
        try {
            BalanceGetStatusDto balanceGetStatusDto = addressCollectService.getBalanceGetStatusDto(Chain.fromCode(chain));
            return GenericDto.success(balanceGetStatusDto);
        } catch (Exception e) {
            log.error("getBalanceGetStatus", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取所有分配给用户地址的余额
     * @param chain
     * @return
     */
    @GetMapping("/mgt/user-address-balances")
    @ApiOperation("获取所有分配给用户地址的余额")
    public GenericDto<List<AddressBalanceDto>> getUserAddressBalances(@RequestParam(value = "chain") int chain,
                                                                      @RequestParam(value = "currency", defaultValue = AccountConstants.QUOTE_CURRENCY) String currency) {
        try {
            List<AddressBalanceDto> balances = addressCollectService.getBalances(Chain.fromCode(chain), currency);
            return GenericDto.success(balances);
        } catch (Exception e) {
            log.error("getUserAddressBalances", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 钱包账户归集
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/mgt/fund-collect")
    @ApiOperation("钱包账户归集")
    public GenericDto<AddressCollectHisDto> createFundCollect(@RequestBody FundCollectRequestDto requestDto) {
        try {
            AddressCollectHisDto hisDto = addressCollectService.createFundCollect(requestDto);
            return GenericDto.success(hisDto);
        } catch (Exception e) {
            log.error("createFundCollect", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取钱包归集历史
     *
     * @param chain
     * @param startTime
     * @param endTime
     * @param fromAddress
     * @param toAddress
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/mgt/fund-collect-history")
    @ApiOperation("获取钱包归集历史")
    public GenericDto<IPage<AddressCollectHisDto>> getFundCollectHistory(@RequestParam("chain") int chain,
                                                                         @RequestParam("startTime") long startTime,
                                                                         @RequestParam("endTime") long endTime,
                                                                         @RequestParam(value = "fromAddress", required = false) String fromAddress,
                                                                         @RequestParam(value = "toAddress", required = false) String toAddress,
                                                                         @RequestParam("page") int page,
                                                                         @RequestParam("size") int size) {
        try {
            IPage<AddressCollectHisDto> list = addressCollectHisService.getHistory(Chain.fromCode(chain), startTime, endTime, fromAddress, toAddress, page, size);
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getFundCollectHistory", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 根据归集订单Id获取钱包归集历史
     *
     * @param orderId
     * @return
     */
    @GetMapping("/mgt/fund-collect-history-by-order")
    @ApiOperation("根据归集订单Id获取钱包归集历史")
    public GenericDto<List<AddressCollectHisDto>> getFundCollectHistoryByOrder(@RequestParam("orderId") long orderId) {
        try {
            List<AddressCollectHisDto> list = addressCollectHisService.getAddressCollectByOrderId(orderId);
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getFundCollectHistoryByOrder", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 创建钱包账户归集订单
     *
     * @param addressCollectOrderRequestDto
     * @return
     */
    @PostMapping("/mgt/fund-collect-order")
    @ApiOperation("创建钱包账户归集订单")
    public GenericDto<AddressCollectOrderHisDto> createFundCollectOrder(@RequestBody AddressCollectOrderRequestDto addressCollectOrderRequestDto) {
        try {
            AddressCollectOrderHisDto hisDto = addressCollectService.createFundCollectOrder(addressCollectOrderRequestDto);
            return GenericDto.success(hisDto);
        } catch (Exception e) {
            log.error("createFundCollectOrder", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取某个钱包账户归集订单
     *
     * @param id
     * @return
     */
    @GetMapping("/mgt/fund-collect-order")
    @ApiOperation("获取某个钱包账户归集订单")
    public GenericDto<AddressCollectOrderHisDto> getFundCollectOrder(@RequestParam("id") long id) {
        try {
            AddressCollectOrderHisDto hisDto = addressCollectHisService.getAddressCollectOrderById(id);
            return GenericDto.success(hisDto);
        } catch (Exception e) {
            log.error("getFundCollectOrder", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取钱包归集历史
     *
     * @param startTime
     * @param endTime
     * @param type
     * @param address
     * @param currency
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/mgt/fund-collect-order-history")
    @ApiOperation("获取钱包归集历史")
    public GenericDto<IPage<AddressCollectOrderHisDto>> getFundCollectOrderHistory(@RequestParam("chain") int chain,
                                                                                      @RequestParam("startTime") long startTime,
                                                                                      @RequestParam("endTime") long endTime,
                                                                                      @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                                                                      @RequestParam(value = "address", required = false) String address,
                                                                                      @RequestParam(value = "currency", required = false) String currency,
                                                                                      @RequestParam("page") int page,
                                                                                      @RequestParam("size") int size) {
        try {
            IPage<AddressCollectOrderHisDto> list = addressCollectHisService.getOrderHistory(Chain.fromCode(chain), startTime, endTime, type, address, currency, page, size);
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getFundCollectOrderHistory", e);
            return Utils.returnFromException(e);
        }
    }




}
