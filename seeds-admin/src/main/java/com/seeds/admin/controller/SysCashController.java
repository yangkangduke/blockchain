package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.seeds.account.dto.BalanceGetStatusDto;
import com.seeds.admin.dto.*;
import com.seeds.admin.service.AssetManagementService;
import com.seeds.admin.service.ISysWithdrawDepositService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@RestController
@RequestMapping("/cash")
@Api(tags = "cash")
public class SysCashController {


    @Autowired
    private ISysWithdrawDepositService mgtWithdrawDepositService;
    @Autowired
    private AssetManagementService assetManagementService;


    @GetMapping("/chain-withdraw-deposit-type")
    @ApiOperation("获取充提币地址链信息列表")
    public GenericDto<List<MgtChainTypeDto>> queryChainWithdrawTypes() {
        List<MgtChainTypeDto> dtos = Lists.newArrayList();
        Chain.SUPPORT_CREATE_ADDRESS_LIST.forEach(chain ->
                dtos.add(MgtChainTypeDto.builder()
                        .code(chain.getCode())
                        .name(chain.getName())
                        .nativeToken(chain.getNativeToken())
                        .decimals(chain.getDecimals())
                        .build())
        );
        return GenericDto.success(dtos);
    }

    @GetMapping("/pending-withdraw/list")
    @ApiOperation("获取提币申请")
    // todo
//    @MgtAuthority(path = "/funds/withdraw-deposit/withdraw/reviews/pending/")
    public GenericDto<IPage<MgtDepositWithdrawDto>> listPendingWithdraw(@RequestParam(value = "currency", required = false) String currency,
                                                                        @RequestParam(value = "userId", required = false) Long userId,
                                                                        @RequestParam("current") Integer current,
                                                                        @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listPendingWithdraw(currency, userId, current, size));

    }

    @PostMapping("/pending-withdraw/approve")
    @ApiOperation("批准提币")
//    @MgtAuthority(path = "/funds/withdraw-deposit/withdraw/reviews/pending/:approve")
    public GenericDto<Boolean> approveWithdraw(@RequestBody @Valid MgtApproveRejectDto dto) {
        return mgtWithdrawDepositService.approvePendingWithdraw(dto);
    }

    @PostMapping("/pending-withdraw/reject")
    @ApiOperation("拒绝提币")
//    @MgtAuthority(path = "/funds/withdraw-deposit/withdraw/reviews/pending/:reject")
    public GenericDto<Boolean> rejectWithdraw(@RequestBody @Valid MgtApproveRejectDto dto) {
        return mgtWithdrawDepositService.rejectPendingWithdraw(dto);
    }

    @GetMapping("/withdraw-history/list")
    @ApiOperation("获取提币审批历史")
//    @MgtAuthority(path = "/funds/withdraw-deposit/withdraw/reviews/history/")
    public GenericDto<Page<MgtDepositWithdrawDto>> listWithdrawHis(@RequestParam(value = "currency", required = false) String currency,
                                                                               @RequestParam(value = "usdId", required = false) Long userId,
                                                                               @RequestParam(value = "status", defaultValue = "0") Integer status,
                                                                               @RequestParam("current") Integer current,
                                                                               @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listWithdrawReviewHis(currency, userId, status, current, size));
    }

    @GetMapping("/pending-deposit/list")
    @ApiOperation("获取充币申请")
//    @MgtAuthority(path = "/funds/withdraw-deposit/deposit/reviews/pending/")
    public GenericDto<Page<MgtDepositWithdrawDto>> listPendingDeposit(@RequestParam(value = "currency", required = false) String currency,
                                                                                  @RequestParam(value = "userId", required = false) Long userId,
                                                                                  @RequestParam("current") Integer current,
                                                                                  @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listPendingDeposit(currency, userId, current, size));
    }

    @PostMapping("/pending-deposit/approve")
    @ApiOperation("批准充币")
//    @MgtAuthority(path = "/funds/withdraw-deposit/deposit/reviews/pending/:approve")
    public GenericDto<Boolean> approveDeposit(@RequestBody @Valid MgtApproveRejectDto dto) {
        dto.setComment("");
        return mgtWithdrawDepositService.approvePendingDeposit(dto);
    }

    @PostMapping("/pending-deposit/reject")
    @ApiOperation("拒绝充币")
//    @MgtAuthority(path = "/funds/withdraw-deposit/deposit/reviews/pending/:reject")
    public GenericDto<Boolean> rejectDeposit(@RequestBody @Valid MgtApproveRejectDto dto) {
        return mgtWithdrawDepositService.rejectPendingDeposit(dto);
    }

    @GetMapping("/deposit-history/list")
    @ApiOperation("获取充币审批历史")
//    @MgtAuthority(path = "/funds/withdraw-deposit/deposit/reviews/history/")
    public GenericDto<Page<MgtDepositWithdrawDto>> listDepositHis(@RequestParam(value = "currency", required = false) String currency,
                                                                  @RequestParam(value = "userId", required = false) Long userId,
                                                                  @RequestParam(value = "status", required = false) Integer status,
                                                                  @RequestParam("current") Integer current,
                                                                  @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listDepositReviewHis(currency, userId, status, current, size));
    }

    @PostMapping("/balance/create-balances-get")
    @ApiOperation("刷新用户充币地址余额")
    public GenericDto<Boolean> createBalanceGet(@RequestParam(value = "chain", defaultValue = "1") int chain) {
        return assetManagementService.createBalanceGet(chain);
    }

    @GetMapping("/balance/status")
    @ApiOperation("获取刷新余额时间")
    public GenericDto<BalanceGetStatusDto> getBalanceGetStatus(@RequestParam(value = "chain", defaultValue = "1") int chain) {
        return assetManagementService.getBalanceGetStatus(chain);
    }

    @GetMapping("/balance/deposit-address")
    @ApiOperation("获取用户充币地址列表")
    public GenericDto<Page<MgtDepositAddressDto>> depositAddresses(@RequestParam(value = "currency", required = false) String currency,
                                                                   @RequestParam(value = "chain", defaultValue = "1") int chain,
                                                                   @RequestParam(value = "address", required = false) String address,
                                                                   @RequestParam(value = "sorter", required = false) String sorter) {
        return assetManagementService.queryDepositAddress(currency, chain, address, sorter);
    }


    @GetMapping("/account/hot-wallet")
    @ApiOperation("获取热钱包列表（所有系统使用的钱包）")
    public GenericDto<Page<MgtHotWalletDto>> hotWallet(@RequestParam(value = "type", required = false) Integer type,
                                                       @RequestParam(value = "chain", defaultValue = "1") int chain,
                                                       @RequestParam(value = "address", required = false) String address,
                                                       @RequestParam(value = "sorter", required = false) String sorter) {
        return assetManagementService.queryHotWallets(type, chain, address, sorter);
    }

    @PostMapping("/transfer/wallet-transfers/list")
    @ApiOperation("获取热钱包划转历史")
    public GenericDto<Page<MgtWalletTransferDto>> listWalletTransfers(@RequestBody MgtWalletTransferQueryDto dto) {
        return assetManagementService.queryWalletTransfers(dto);
    }


    @PostMapping("/transfer/wallet-transfers/transfer")
    @ApiOperation("发起热钱包划转")
    public GenericDto<String> walletTransfer(@RequestBody @Valid MgtWalletTransferRequestDto dto) {
        return assetManagementService.walletTransfer(dto);
    }

    @GetMapping("/transfer/wallet-transfers/gas-config")
    @ApiOperation("获取热钱包划转gas费用")
    public GenericDto<MgtGasConfig> gasConfig(@RequestParam(value = "chain", defaultValue = "1") int chain) {
        return assetManagementService.getGasConfig(chain);
    }

    @GetMapping("/transfer/history-list")
    @ApiOperation("获取资金归集历史记录列表")
    public GenericDto<Page<MgtAddressCollectOrderHisDto>> queryHistoryList(
            @RequestParam(value = "currency", required = false) String currency,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "chain", defaultValue = "1") int chain,
            @RequestParam("current") int current,
            @RequestParam("pageSize") int pageSize) {
        if (isBlank(currency)) {
            currency = null;
        }
        if (isBlank(address)) {
            address = null;
        }
        return assetManagementService.getFundCollectOrderHistory(chain, currency, address, current, pageSize, 1);
    }

    @GetMapping("/transfer/gas-fee-list")
    @ApiOperation("获取Gas Fee划转历史记录列表")
    public GenericDto<Page<MgtAddressCollectOrderHisDto>> queryGasFeeList(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "chain", defaultValue = "1") int chain,
            @RequestParam("current") int current,
            @RequestParam("pageSize") int pageSize) {
        if (isBlank(address)) {
            address = null;
        }
        return assetManagementService.getFundCollectOrderHistory(chain, null, address, current, pageSize, 2);
    }

    @PostMapping("/transfer/create-order")
    @ApiOperation("用户资金归集")
    public GenericDto<Boolean> createOrder(@RequestBody MgtAddressCollectOrderRequestDto dto) {
        return assetManagementService.createOrder(dto);
    }


    @PostMapping("/transfer/gas-fee/create-order")
    @ApiOperation("Gas Fee划转")
    public GenericDto<Boolean> createGasFeeOrder(@RequestBody MgtAddressCollectOrderRequestDto dto) {
        return assetManagementService.createGasFeeOrder(dto);
    }

}
