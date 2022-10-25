package com.seeds.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.account.dto.*;
import com.seeds.account.service.IAccountService;
import com.seeds.account.service.IAddressCollectHisService;
import com.seeds.account.service.IAddressCollectService;
import com.seeds.account.service.IChainActionService;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.dto.PagedDto;
import com.seeds.common.enums.Chain;
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
    IAddressCollectHisService addressCollectHisService;
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
            addressCollectService.scanCollect();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("scanWithdraw", e);
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


    /**
     * 定期收集待归集地址余额
     *
     * @return
     */
    @PostMapping("/job/fund-collect-scan-pending-balances")
    @ApiOperation("定期收集待归集地址余额")
    public GenericDto<Boolean> scanPendingCollectBalances() {
        try {
            addressCollectService.scanPendingCollectBalances();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("scanPendingCollectBalances", e);
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
    @ApiOperation("钱包账户归集")
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
