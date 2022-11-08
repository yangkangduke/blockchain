package com.seeds.account.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.*;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.*;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(name = "seeds-account", url = "${service.url.account}",path = "/account-internal", configuration = {AccountFeignInnerRequestInterceptor.class})
public interface AccountFeignClient {

    /**
     * 负责创建空闲地址
     *
     * @return
     */
    @PostMapping("/job/scan-and-create-addresses")
    GenericDto<Boolean> scanAndCreateAddresses();

    /**
     * 扫描新的块
     *
     * @return
     */
    @PostMapping("/job/scan-block")
    GenericDto<Boolean> scanBlock();

    /**
     * 执行提币
     *
     * @return
     */
    @PostMapping("/job/execute-withdraw")
    GenericDto<Boolean> executeWithdraw();

    /**
     * 扫描提币状态
     *
     * @return
     */
    @PostMapping("/job/scan-withdraw")
    GenericDto<Boolean> scanWithdraw();

    /**
     * 获取链上原始交易list
     *
     * @return
     */
    @PostMapping("/sys/chain/transaction")
    GenericDto<Page<ChainTxnDto>> getChainTxnList(@RequestBody @Valid ChainTxnPageReq req);

    /**
     * 执行重发功能
     *
     * @return
     */
    @PostMapping("/sys/chain/replay/execute")
    GenericDto<Boolean> executeChainReplay(@RequestBody @Valid ChainTxnReplayDto chainTxnReplayDto);

    /**
     * 获取链上替换交易list
     *
     * @return
     */
    @PostMapping("/sys/chain/replacement")
    GenericDto<Page<ChainTxnDto>> getChainTxnReplaceList(@RequestBody @Valid ChainTxnPageReq req);

    /**
     * 执行替换功能
     *
     * @return
     */
    @PostMapping("/sys/chain/replacement/execute")
    GenericDto<Long> executeChainReplacement(@RequestBody @Valid ChainTxnReplaceDto chainTxnReplaceDto);

    /**
     * 获取需要审核的充提
     *
     * @return
     */
    @PostMapping("/sys/pending-transaction")
    GenericDto<Page<ChainDepositWithdrawHisDto>> getPendingTransactions(@RequestBody AccountPendingTransactionsReq transactionsReq);

    /**
     * 充币提币审核通过
     *
     * @param approveRejectDto
     * @return
     */
    @PostMapping("/sys/approve-transaction")
    GenericDto<Boolean> approveTransaction(@RequestBody ApproveRejectDto approveRejectDto);


    /**
     * 充币提币审核拒绝
     *
     * @param approveRejectDto
     * @return
     */
    @PostMapping("/sys/reject-transaction")
    GenericDto<Boolean> rejectTransaction(@RequestBody ApproveRejectDto approveRejectDto);

    /**
     * 获取人工处理过的充币提币
     *
     * @return
     */
    @PostMapping("/sys/processed-transaction")
    GenericDto<Page<ChainDepositWithdrawHisDto>> getManualProcessedTransactions(@RequestBody AccountPendingTransactionsReq transactionsReq);


    /**
     * 定期收集待归集地址余额
     *
     * @return
     */
    @PostMapping("/job/fund-collect-scan-pending-balances")
    GenericDto<Boolean> scanPendingCollectBalances();


    @GetMapping("/sys/pending-collect-balances")
    GenericDto<Map<Chain, Map<String, BigDecimal>>> getPendingCollectBalances() throws Exception;
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
    @GetMapping("/sys/fund-collect-order-history")
    GenericDto<Page<AddressCollectOrderHisDto>> getFundCollectOrderHistory(@RequestParam("chain") int chain,
                                                                           @RequestParam("startTime") long startTime,
                                                                           @RequestParam("endTime") long endTime,
                                                                           @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                                                           @RequestParam(value = "address", required = false) String address,
                                                                           @RequestParam(value = "currency", required = false) String currency,
                                                                           @RequestParam("page") int page,
                                                                           @RequestParam("size") int size);


    @GetMapping("/sys/fund-collect-history")
    GenericDto<Page<AddressCollectHisDto>> getFundCollectHistory(@RequestParam("chain") int chain,
                                                                     @RequestParam("startTime") long startTime,
                                                                     @RequestParam("endTime") long endTime,
                                                                     @RequestParam(value = "fromAddress", required = false) String fromAddress,
                                                                     @RequestParam(value = "toAddress", required = false) String toAddress,
                                                                     @RequestParam("page") int page,
                                                                     @RequestParam("size") int size);

    /**
     * 根据归集订单Id获取钱包归集历史
     *
     * @param orderId
     * @return
     */
    @GetMapping("/sys/fund-collect-history-by-order")
    GenericDto<List<AddressCollectHisDto>> getFundCollectHistoryByOrder(@RequestParam("orderId") long orderId);


    /**
     * 创建钱包账户归集订单
     *
     * @param addressCollectOrderRequestDto
     * @return
     */
    @PostMapping("/sys/fund-collect-order")
    GenericDto<AddressCollectOrderHisDto> createFundCollectOrder(@RequestBody AddressCollectOrderRequestDto addressCollectOrderRequestDto);

    /**
     * 获取某个钱包账户归集订单
     *
     * @param id
     * @return
     */
    @GetMapping("/sys/fund-collect-order")
    GenericDto<AddressCollectOrderHisDto> getFundCollectOrder(@RequestParam("id") long id);


    /**
     * 获取所有分配给用户地址的余额
     *
     * @param chain
     * @return
     */
    @GetMapping("/sys/user-address-balances")
    GenericDto<List<AddressBalanceDto>> getUserAddressBalances(@RequestParam(value = "chain") int chain, @RequestParam(value = "currency", defaultValue = AccountConstants.USDT) String currency);


    /**
     * 创建获取用户充币地址余额的请求
     *
     * @param chain
     * @return
     */
    @PostMapping("/sys/create-balances-get")
    GenericDto<Boolean> createBalanceGet(@RequestParam("chain") int chain);

    /**
     * 查询获取用户充币地址余额的请求的状态
     *
     * @param chain
     * @return
     */
    @GetMapping("/sys/get-balances-get-status")
    GenericDto<BalanceGetStatusDto> getBalanceGetStatus(@RequestParam(value = "chain") int chain);

    /**
     * 获取所有系统使用的钱包地址
     *
     * @param chain
     * @return
     */
    @GetMapping("/sys/system-wallet-address")
    GenericDto<List<SystemWalletAddressDto>> getAllSystemWalletAddress(@RequestParam("chain") int chain);


    /**
     * 获取所有系统地址的余额
     *
     * @param chain
     * @return
     */
    @GetMapping("/sys/system-address-balances")
    GenericDto<List<AddressBalanceDto>> getSystemAddressBalances(@RequestParam("chain") int chain);


    /**
     * 钱包归集
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/sys/fund-collect")
    GenericDto<AddressCollectHisDto> createFundCollect(@RequestBody FundCollectRequestDto requestDto);

    /**
     * 获取chain GasLimit
     *
     * @return
     */
    @GetMapping("/sys/gas-limit")
    GenericDto<Long> getGasLimit(@RequestParam(value = "chain") int chain);

    /**
     * 获取链上最新 gas price
     *
     * @param chain
     * @return
     */
    @PostMapping("/sys/gas-price")
    GenericDto<ChainGasPriceDto> getGasPrice(@RequestParam(value = "chain") int chain);

    /**
     * 获取链上最新的GasPrice
     *
     * @return
     */
    @PostMapping("/job/get-and-metric-current-gas-price-oracle")
    GenericDto<Boolean> getAndMetricCurrentGasPriceOracle();

    /**
     * 获取所有提币白名单
     *
     * @return
     */
    @GetMapping("/sys/withdraw-whitelist-address")
    GenericDto<List<WithdrawWhitelistDto>> getAllWithdrawWhitelist();

    /**
     * 添加提币白名单
     *
     * @param withdrawWhitelistDto
     * @return
     */
    @PostMapping("/sys/add-withdraw-whitelist-address")
    GenericDto<Boolean> addWithdrawWhitelist(@RequestBody WithdrawWhitelistDto withdrawWhitelistDto);

    /**
     * 更新提币白名单
     *
     * @param withdrawWhitelistDto
     * @return
     */
    @PostMapping("/sys/update-withdraw-whitelist-address")
    GenericDto<Boolean> updateWithdrawWhitelist(@RequestBody WithdrawWhitelistDto withdrawWhitelistDto);

    /**
     * 获取所有充提币黑地址
     *
     * @param type 1:充币 2:提币
     * @return
     */
    @GetMapping("/sys/blacklist-address")
    GenericDto<List<BlacklistAddressDto>> getAllBlacklistAddress(@RequestParam("type") int type);

    /**
     * 添加新充提币黑地址
     *
     * @param blacklistAddressDto
     * @return
     */
    @PostMapping("/sys/add-blacklist-address")
    GenericDto<Boolean> addBlacklistAddress(@RequestBody BlacklistAddressDto blacklistAddressDto);

    /**
     * 更新充提币黑地址
     *
     * @param blacklistAddressDto
     * @return
     */
    @PostMapping("/sys/update-blacklist-address")
    GenericDto<Boolean> updateBlacklistAddress(@RequestBody BlacklistAddressDto blacklistAddressDto);

    /**
     * 删除充提币黑地址
     *
     * @param blacklistAddressDto
     * @return
     */
    @PostMapping("/sys/delete-blacklist-address")
    GenericDto<Boolean> deleteBlacklistAddress(@RequestBody BlacklistAddressDto blacklistAddressDto);

}
