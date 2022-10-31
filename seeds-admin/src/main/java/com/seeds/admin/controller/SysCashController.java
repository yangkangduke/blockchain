package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.seeds.account.dto.AddressCollectHisDto;
import com.seeds.account.dto.BalanceGetStatusDto;
import com.seeds.account.dto.ChainGasPriceDto;
import com.seeds.account.enums.FundCollectOrderType;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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


    /*@ApiImplicitParams({
            //参数效验
            @ApiImplicitParam(name="currency",value="USDT",required=false,dataType="String"),
            @ApiImplicitParam(name="userId",value="用户id",required=false,dataType="Long"),
            @ApiImplicitParam(name="current",value="当前页码",required=true,dataType="Integer"),
            @ApiImplicitParam(name="size",value="当前页数据量",required=true,dataType="Integer")
    })*/
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

    @GetMapping("/chain-type")
    @ApiOperation("获取支持的币种类型")
    public GenericDto<List<MgtChainTypeDto>> queryChainTypes() {
        List<MgtChainTypeDto> dtos = Lists.newArrayList();
        Chain.SUPPORT_LIST.forEach(chain ->
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
    @ApiOperation(value = "获取提币申请")
    public GenericDto<IPage<MgtDepositWithdrawDto>> listPendingWithdraw(@RequestParam(value = "currency", required = false) String currency,
                                                                        @RequestParam(value = "userId", required = false) Long userId,
                                                                        @RequestParam("current") Integer current,
                                                                        @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listPendingWithdraw(currency, userId, current, size));

    }

    @PostMapping("/pending-withdraw/approve")
    @ApiOperation(value = "批准提币", notes = "只需要传id、comment参数")
    public GenericDto<Boolean> approveWithdraw(@RequestBody @Valid MgtApproveRejectDto dto) {
        return mgtWithdrawDepositService.approvePendingWithdraw(dto);
    }

    @PostMapping("/pending-withdraw/reject")
    @ApiOperation(value = "拒绝提币", notes = "只需要传id、comment参数")
    public GenericDto<Boolean> rejectWithdraw(@RequestBody @Valid MgtApproveRejectDto dto) {
        return mgtWithdrawDepositService.rejectPendingWithdraw(dto);
    }


    @GetMapping("/withdraw-history/list")
    @ApiOperation(value = "获取提币审批历史")
    public GenericDto<Page<MgtDepositWithdrawDto>> listWithdrawHis( String currency,
                                                                               Long userId,
                                                                               Integer status,
                                                                               Integer current,
                                                                               Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listWithdrawReviewHis(currency, userId, status, current, size));
    }


    @GetMapping("/pending-deposit/list")
    @ApiOperation(value = "获取充币申请")
    public GenericDto<Page<MgtDepositWithdrawDto>> listPendingDeposit(@RequestParam(value = "currency", required = false) String currency,
                                                                                  @RequestParam(value = "userId", required = false) Long userId,
                                                                                  @RequestParam("current") Integer current,
                                                                                  @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listPendingDeposit(currency, userId, current, size));
    }

    @PostMapping("/pending-deposit/approve")
    @ApiOperation(value = "批准充币", notes = "只需要传id、comment参数")
    public GenericDto<Boolean> approveDeposit(@RequestBody @Valid MgtApproveRejectDto dto) {
        dto.setComment("");
        return mgtWithdrawDepositService.approvePendingDeposit(dto);
    }

    @PostMapping("/pending-deposit/reject")
    @ApiOperation(value = "拒绝充币", notes = "只需要传id、comment参数")
    public GenericDto<Boolean> rejectDeposit(@RequestBody @Valid MgtApproveRejectDto dto) {
        return mgtWithdrawDepositService.rejectPendingDeposit(dto);
    }


    @GetMapping("/deposit-history/list")
    @ApiOperation(value = "获取充币审批历史")
    public GenericDto<Page<MgtDepositWithdrawDto>> listDepositHis(@RequestParam(value = "currency", required = false) String currency,
                                                                  @RequestParam(value = "userId", required = false) Long userId,
                                                                  @RequestParam(value = "status", required = false) Integer status,
                                                                  @RequestParam("current") Integer current,
                                                                  @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listDepositReviewHis(currency, userId, status, current, size));
    }


    @GetMapping("/balance/pending-collect-balances")
    @ApiOperation("获取所有用户的待归集余额")
    public GenericDto<Map<Chain, Map<String, BigDecimal>>> getPendingCollectBalances() throws Exception {
        return assetManagementService.getPendingCollectBalances();
    }

    @GetMapping("/transfer/wallet-transfers/gas-config")
    @ApiOperation("获取划转gas费用")
    public GenericDto<MgtGasConfig> gasConfig(@RequestParam(value = "chain", defaultValue = "1") int chain) {
        return assetManagementService.getGasConfig(chain);
    }

    @GetMapping("/transfer/wallet-transfers/gas-price")
    @ApiOperation("获取划转gasPrice")
    public GenericDto<ChainGasPriceDto> getGasPrice(@RequestParam(value = "chain", defaultValue = "1") int chain) {
        return assetManagementService.getGasPrice(chain);
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
    @ApiOperation("获取所有分配给用户地址的余额")
    public GenericDto<Page<MgtDepositAddressDto>> depositAddresses(@RequestParam(value = "currency", required = false) String currency,
                                                                   @RequestParam(value = "chain", defaultValue = "1") int chain,
                                                                   @RequestParam(value = "address", required = false) String address) {
        return assetManagementService.queryDepositAddress(currency, chain, address);
    }


    @GetMapping("/account/hot-wallet")
    @ApiOperation("获取热钱包列表（所有系统使用的钱包）")
    public GenericDto<Page<MgtHotWalletDto>> hotWallet(@RequestParam(value = "type", required = false) Integer type,
                                                       @RequestParam(value = "chain", defaultValue = "1") int chain,
                                                       @RequestParam(value = "address", required = false) String address) {
        return assetManagementService.queryHotWallets(type, chain, address);
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

    @GetMapping("/transfer/history-list")
    @ApiOperation("获取资金归集历史记录列表--USER_TO_SYSTEM")
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
        return assetManagementService.getFundCollectOrderHistory(chain, currency, address, current, pageSize, FundCollectOrderType.FROM_USER_TO_SYSTEM.getCode());
    }

    @GetMapping("/transfer/gas-fee-list")
    @ApiOperation("获取Gas Fee划转历史记录列表----SYSTEM_TO_USER")
    public GenericDto<Page<MgtAddressCollectOrderHisDto>> queryGasFeeList(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "chain", defaultValue = "1") int chain,
            @RequestParam("current") int current,
            @RequestParam("pageSize") int pageSize) {
        if (isBlank(address)) {
            address = null;
        }
        return assetManagementService.getFundCollectOrderHistory(chain, null, address, current, pageSize, FundCollectOrderType.FROM_SYSTEM_TO_USER.getCode());
    }

    @GetMapping("/transfer/collect-history-by-order")
    @ApiOperation("根据归集订单Id获取钱包归集历史")
    public GenericDto<List<AddressCollectHisDto>> getFundCollectHistoryByOrder(@RequestParam(value = "orderId") long orderId) {
        return assetManagementService.getAddressCollectByOrderId(orderId);
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
