package com.seeds.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.*;
import com.seeds.account.dto.req.*;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.enums.DepositStatus;
import com.seeds.account.enums.WithdrawStatus;
import com.seeds.account.model.SwitchReq;
import com.seeds.account.service.*;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private IAddressCollectHisService addressCollectHisService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IChainDepositWithdrawHisService chainDepositWithdrawHisService;
    @Autowired
    private IChainService chainService;
    @Autowired
    private IDepositRuleService depositRuleService;
    @Autowired
    private IWithdrawRuleService withdrawRuleService;
    @Autowired
    private IWithdrawLimitRuleService withdrawLimitRuleService;
    @Autowired
    private ISystemWalletAddressService systemWalletAddressService;
    @Autowired
    private IWithdrawWhitelistService withdrawWhitelistService;
    @Autowired
    private IBlacklistAddressService blacklistAddressService;
    @Autowired
    private ISystemConfigService systemConfigService;

    @Autowired
    private IActionControlService actionControlService;

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


    @PostMapping("/sys/pending-transaction")
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


    @PostMapping("/sys/processed-transaction")
    @ApiOperation("获取已审核的充提")
    @Inner
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
    @ApiOperation("扫描提币、归集的状态")
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


    /**
     * 获取待归集余额
     *
     * @return
     */
    @GetMapping("/sys/pending-collect-balances")
    @ApiOperation("获取待归集余额")
    @Inner
    public GenericDto<Map<Chain, Map<String, BigDecimal>>> getPendingCollectBalances() {
        return GenericDto.success(addressCollectService.getPendingCollectBalances());
    }

    /**
     * 创建获取用户充币地址余额的请求
     * @param chain
     * @return
     */
    @PostMapping("/sys/create-balances-get")
    @ApiOperation("创建获取用户充币地址余额的请求")
    @Inner
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
    @GetMapping("/sys/get-balances-get-status")
    @ApiOperation("查询获取用户充币地址余额的请求的状态")
    @Inner
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
    @GetMapping("/sys/user-address-balances")
    @ApiOperation("获取所有分配给用户地址的余额")
    @Inner
    public GenericDto<List<AddressBalanceDto>> getUserAddressBalances(@RequestParam(value = "chain") int chain,
                                                                      @RequestParam(value = "currency", defaultValue = AccountConstants.USDT) String currency) {
        try {
            List<AddressBalanceDto> balances = addressCollectService.getBalances(Chain.fromCode(chain), currency);
            return GenericDto.success(balances);
        } catch (Exception e) {
            log.error("getUserAddressBalances", e);
            return Utils.returnFromException(e);
        }
    }


    /**
     * 获取 chain GasLimit
     *
     * @return
     */
    @GetMapping("/sys/gas-limit")
    @ApiOperation("获取币种划转时的GasLimit")
    @Inner
    public GenericDto<Long> getGasLimit(@RequestParam(value = "chain") int chain) {
        try {
            return GenericDto.success(chainService.getGasLimit(Chain.fromCode(chain)));
        } catch (Exception e) {
            log.error("getGasLimit", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取链上最新 gas price
     *
     * @param chain
     * @return
     */
    @PostMapping("/sys/gas-price")
    @ApiOperation("获取链上最新 gas price")
    @Inner
    public GenericDto<ChainGasPriceDto> getGasPrice(@RequestParam(value = "chain") int chain) {
        try {
            return GenericDto.success(chainService.getGasPriceDto(Chain.fromCode(chain)));
        } catch (Exception e) {
            log.error("getGasPrice", e);
            return Utils.returnFromException(e);
        }
    }


    /**
     * 定时任务-获取链上最新的GasPrice
     * 并缓存到Redis
     *
     * @return
     */
    @PostMapping("/job/get-and-metric-current-gas-price-oracle")
    @ApiOperation("获取链上最新的GasPrice")
    @Inner
    public GenericDto<Boolean> getAndMetricCurrentGasPriceOracle() {
        try {
            chainActionService.getAndMetricCurrentGasPriceOracle();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("getAndMetricCurrentGasPriceOracle", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取所有系统使用的地址
     *
     * @param chain
     * @return
     */
    @GetMapping("/sys/system-wallet-address")
    @ApiOperation("获取所有系统使用的地址")
    @Inner
    public GenericDto<List<SystemWalletAddressDto>> getAllSystemWalletAddress(@RequestParam("chain") int chain) {
        try {
            List<SystemWalletAddressDto> list = systemWalletAddressService.loadAll()
                    .stream().filter(e -> e.getStatus() == CommonStatus.ENABLED.getCode() && e.getChain() == chain)
                    .collect(Collectors.toList());
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getAllSystemWalletAddress", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取所有系统地址的余额
     *
     * @param chain
     * @return
     */
    @GetMapping("/sys/system-address-balances")
    @ApiOperation("获取所有系统地址的余额")
    @Inner
    public GenericDto<List<AddressBalanceDto>> getSystemAddressBalances(@RequestParam("chain") int chain) {
        try {
            List<String> addresses = systemWalletAddressService.getAll()
                    .stream()
                    .filter(e -> e.getStatus() == CommonStatus.ENABLED.getCode() && e.getChain() == chain)
                    .map(SystemWalletAddressDto::getAddress)
                    .collect(Collectors.toList());
            List<AddressBalanceDto> balances = chainService.getBalancesOnBatch(Chain.fromCode(chain), addresses, 0L);
            return GenericDto.success(balances);
        } catch (Exception e) {
            log.error("getSystemAddressBalances", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 钱包归集
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/sys/fund-collect")
    @ApiOperation("钱包归集")
    @Inner
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
     * (热钱包划转历史)
     * @param chain
     * @param startTime
     * @param endTime
     * @param fromAddress
     * @param toAddress
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/sys/fund-collect-history")
    @ApiOperation("获取钱包归集历史")
    @Inner
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
    @GetMapping("/sys/fund-collect-history-by-order")
    @ApiOperation("根据归集订单Id获取钱包归集历史")
    @Inner
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
    @PostMapping("/sys/fund-collect-order")
    @ApiOperation("创建钱包账户归集订单")
    @Inner
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
    @GetMapping("/sys/fund-collect-order")
    @ApiOperation("获取某个钱包账户归集订单")
    @Inner
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
    @GetMapping("/sys/fund-collect-order-history")
    @ApiOperation("获取钱包归集订单历史")
    @Inner
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

    @GetMapping("/sys/withdraw-whitelist-address")
    @ApiOperation("获取所有提币白名单")
    @Inner
    public GenericDto<List<WithdrawWhitelistDto>> getAllWithdrawWhitelist() {
        try {
            List<WithdrawWhitelistDto> list = withdrawWhitelistService.loadAll();
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getAllWithdrawWhitelist", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/add-withdraw-whitelist-address")
    @ApiOperation("添加提币白名单")
    @Inner
    public GenericDto<Boolean> addWithdrawWhitelist(@RequestBody WithdrawWhitelistDto withdrawWhitelistDto) {
        try {
            withdrawWhitelistService.add(withdrawWhitelistDto);
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("addWithdrawWhitelist", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/update-withdraw-whitelist-address")
    @ApiOperation("更新提币白名单")
    @Inner
    public GenericDto<Boolean> updateWithdrawWhitelist(@RequestBody WithdrawWhitelistDto withdrawWhitelistDto) {
        try {
            withdrawWhitelistService.update(withdrawWhitelistDto);
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("updateWithdrawWhitelist", e);
            return Utils.returnFromException(e);
        }
    }

    @GetMapping("/sys/blacklist-address")
    @ApiOperation("获取所有充提币黑地址")
    @Inner
    public GenericDto<List<BlacklistAddressDto>> getAllBlacklistAddress(@RequestParam("type") int type) {
        try {
            List<BlacklistAddressDto> list = blacklistAddressService.loadAll()
                    .stream().filter(e -> e.getType() == type).collect(Collectors.toList());
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getAllBlacklistAddress", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/add-blacklist-address")
    @ApiOperation("添加新充提币黑地址")
    @Inner
    public GenericDto<Boolean> addBlacklistAddress(@RequestBody BlacklistAddressDto blacklistAddressDto) {
        try {
            blacklistAddressService.add(blacklistAddressDto);
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("addBlacklistAddress", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/update-blacklist-address")
    @ApiOperation("更新充提币黑地址")
    @Inner
    public GenericDto<Boolean> updateBlacklistAddress(@RequestBody BlacklistAddressDto blacklistAddressDto) {
        try {
            blacklistAddressService.update(blacklistAddressDto);
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("updateBlacklistAddress", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/delete-blacklist-address")
    @ApiOperation("删除充提币黑地址")
    @Inner
    public GenericDto<Boolean> deleteBlacklistAddress(@RequestBody BlacklistAddressDto blacklistAddressDto) {
        try {
            blacklistAddressService.delete(blacklistAddressDto);
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("deleteBlacklistAddress", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 创建热钱包地址
     * @param chain
     * @return
     */
    @PostMapping("/sys/create-system-wallet-address")
    @ApiOperation(value = "创建热钱包地址",notes = "chain的值：1 eth， 3 tron")
    @Inner
    public GenericDto<SystemWalletAddressDto> createSystemWalletAddress(@RequestParam("chain") int chain) {
        try {
            SystemWalletAddressDto systemWalletAddressDto = chainActionService.createSystemWalletAddress(Chain.fromCode(chain));
            return GenericDto.success(systemWalletAddressDto);
        } catch (Exception e) {
            log.error("createSystemWalletAddress", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/add-system-wallet-address")
    @ApiOperation("添加系统使用的地址(暂时没用)")
    @Inner
    public GenericDto<Boolean> addSystemWalletAddress(@RequestBody SystemWalletAddressDto systemWalletAddressDto) {
        try {
            systemWalletAddressService.add(systemWalletAddressDto);
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("addSystemWalletAddress", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/update-system-wallet-address")
    @ApiOperation("更新系统使用的地址(暂时没用)")
    @Inner
    public GenericDto<Boolean> updateSystemWalletAddress(@RequestBody SystemWalletAddressDto systemWalletAddressDto) {
        try {
            systemWalletAddressService.update(systemWalletAddressDto);
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("updateSystemWalletAddress", e);
            return Utils.returnFromException(e);
        }
    }

    @GetMapping("/sys/account-system-config-list")
    @ApiOperation("获取账户系统配置")
    @Inner
    public GenericDto<List<AccountSystemConfigDto>> accountSystemConfigList() {
        try {
            return GenericDto.success(systemConfigService.accountSystemConfigList());
        } catch (Exception e) {
            log.error("accountSystemConfigList", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/account-system-config-modify")
    @ApiOperation("编辑账户系统配置")
    @Inner
    public GenericDto<Object> accountSystemConfigModify(@RequestBody AccountSystemConfigDto req) {
        try {
            systemConfigService.accountSystemConfigModify(req);
            return GenericDto.success(null);
        } catch (Exception e) {
            log.error("accountSystemConfigModify", e);
            return Utils.returnFromException(e);
        }
    }
    /**
     * 获取充币规则列表
     *
     * @param req
     * @return
     */
    @PostMapping("/sys/get-deposit-rule-list")
    @Inner
    public GenericDto<List<DepositRuleDto>> getDepositRuleList(@RequestBody DepositRuleReq req) {
        try {
            return GenericDto.success(depositRuleService.getList(req));
        } catch (Exception e) {
            log.error("get-deposit-rule-list", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 新增充币规则
     *
     * @param req
     * @return
     */
    @PostMapping("/sys/add-deposit-rule")
    @Inner
    public GenericDto<Boolean> addDepositRule(@RequestBody DepositRuleSaveOrUpdateReq req) {
        try {
            return GenericDto.success(depositRuleService.add(req));
        } catch (Exception e) {
            log.error("add-deposit-rule", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 编辑充币规则
     *
     * @param req
     * @return
     */
    @PutMapping("/sys/update-deposit-rule")
    @Inner
    public GenericDto<Boolean> updateDepositRule(@RequestBody DepositRuleSaveOrUpdateReq req) {
        try {
            return GenericDto.success(depositRuleService.update(req));
        } catch (Exception e) {
            log.error("update-deposit-rule", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 删除充币规则(启用/禁用)
     *
     * @param req
     * @return
     */
    @PostMapping("/sys/delete-deposit-rule")
    @Inner
    public GenericDto<Boolean> deleteDepositRule(@Valid @RequestBody SwitchReq req) {
        try {
            return GenericDto.success(depositRuleService.delete(req));
        } catch (Exception e) {
            log.error("delete-deposit-rule", e);
            return Utils.returnFromException(e);
        }
    }


    /**
     * 获取提币规则列表
     *
     * @param req
     * @return
     */
    @PostMapping("/sys/get-withdraw-rule-list")
    @Inner
    public GenericDto<List<WithdrawRuleDto>> getWithdrawRuleList(@RequestBody WithdrawRuleReq req) {
        try {
            return GenericDto.success(withdrawRuleService.getList(req));
        } catch (Exception e) {
            log.error("get-withdraw-rule-list", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 新增提币规则
     *
     * @param req
     * @return
     */
    @PostMapping("/sys/add-withdraw-rule")
    @Inner
    public GenericDto<Boolean> addWithdrawRule(@RequestBody WithdrawRuleSaveOrUpdateReq req) {
        try {
            return GenericDto.success(withdrawRuleService.add(req));
        } catch (Exception e) {
            log.error("add-withdraw-rule", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 编辑提币规则
     *
     * @param req
     * @return
     */
    @PutMapping("/sys/update-withdraw-rule")
    @Inner
    public GenericDto<Boolean> updateWithdrawRule(@RequestBody WithdrawRuleSaveOrUpdateReq req) {
        try {
            return GenericDto.success(withdrawRuleService.update(req));
        } catch (Exception e) {
            log.error("update-withdraw-rule", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 删除提币规则
     *
     * @param req
     * @return
     */
    @PostMapping("/sys/delete-withdraw-rule")
    @Inner
    public GenericDto<Boolean> deleteWithdrawRule(@Valid @RequestBody SwitchReq req) {
        try {
            return GenericDto.success(withdrawRuleService.delete(req));
        } catch (Exception e) {
            log.error("delete-withdraw-rule", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 获取提币限额规则列表
     *
     * @return
     */
    @PostMapping("/sys/get-withdraw-limit-list")
    @Inner
    public GenericDto<List<WithdrawLimitRuleDto>> getWithdrawLimitRuleList() {
        try {
            return GenericDto.success(withdrawLimitRuleService.getList());
        } catch (Exception e) {
            log.error("get-withdraw-rule-list", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 新增提币规则
     *
     * @param req
     * @return
     */
    @PostMapping("/sys/add-withdraw-limit")
    @Inner
    public GenericDto<Boolean> addWithdrawLimitRule(@RequestBody WithdrawLimitSaveOrUpdateReq req) {
        try {
            return GenericDto.success(withdrawLimitRuleService.add(req));
        } catch (Exception e) {
            log.error("add-withdraw-rule", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 编辑提币规则
     *
     * @param req
     * @return
     */
    @PutMapping("/sys/update-withdraw-limit")
    @Inner
    public GenericDto<Boolean> updateWithdrawLimitRule(@RequestBody WithdrawLimitSaveOrUpdateReq req) {
        try {
            return GenericDto.success(withdrawLimitRuleService.update(req));
        } catch (Exception e) {
            log.error("update-withdraw-limit-rule", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 删除提币规则
     *
     * @param req
     * @return
     */
    @PostMapping("/sys/delete-withdraw-limit")
    @Inner
    public GenericDto<Boolean> deleteWithdrawLimitRule(@Valid @RequestBody ListReq req) {
        try {
            return GenericDto.success(withdrawLimitRuleService.delete(req));
        } catch (Exception e) {
            log.error("delete-withdraw-limit-rule", e);
            return Utils.returnFromException(e);
        }
    }

    @GetMapping("/sys/action-control")
    @ApiOperation("获取所有系统操作控制")
    @Inner
    public GenericDto<List<ActionControlDto>> getAllActionControl() {
        try {
            List<ActionControlDto> list = actionControlService.loadAll();
            return GenericDto.success(list);
        } catch (Exception e) {
            log.error("getAllActionControl", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/add-action-control")
    @ApiOperation("添加新系统操作控制")
    @Inner
    public GenericDto<Boolean> addActionControl(@RequestBody ActionControlDto actionControlDto) {
        try {
            actionControlService.add(actionControlDto);
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("addActionControl", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/sys/update-action-control")
    @ApiOperation("更新系统操作控制")
    @Inner
    public GenericDto<Boolean> updateActionControl(@RequestBody ActionControlDto actionControlDto) {
        try {
            actionControlService.update(actionControlDto);
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("updateActionControl", e);
            return Utils.returnFromException(e);
        }
    }

}
