package com.seeds.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.*;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.*;
import com.seeds.account.enums.FundCollectOrderType;
import com.seeds.account.ex.AccountException;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.annotation.AuditLog;
import com.seeds.admin.dto.*;
import com.seeds.admin.enums.Action;
import com.seeds.admin.enums.Module;
import com.seeds.admin.enums.SubModule;
import com.seeds.admin.mapstruct.AssetManagementMapper;
import com.seeds.admin.mapstruct.WalletTransferRequestMapper;
import com.seeds.admin.service.AssetManagementService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.enums.ErrorCode;
import com.seeds.uc.enums.CurrencyEnum;
import jodd.bean.BeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.seeds.account.enums.ChainExchangeAction.SYSTEM_EXCHANGE;
import static com.seeds.admin.constant.MgtConstants.*;
import static com.seeds.admin.enums.AccountType.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author: hewei
 * @date 2022/10/26
 */
@Service
public class AssetManagementServiceImpl implements AssetManagementService {
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private AssetManagementMapper assetManagementMapper;

    @Autowired
    private WalletTransferRequestMapper walletTransferRequestMapper;

    private final static Map<String, Comparator<MgtAccountAssetDto>> sorterMap = Maps.newHashMap();
    private final static Map<String, Comparator<MgtHotWalletDto>> sorterHotWalletMap = Maps.newHashMap();
    public final static RangeMap<Long, Integer> accountTypeRm = TreeRangeMap.create();
    private final static Map<String, Comparator<MgtDepositAddressDto>> sorterDepositAddressMap = Maps.newHashMap();

    {
        accountTypeRm.put(Range.range(0L, BoundType.CLOSED, AccountConstants.getSystemUserId(), BoundType.OPEN), OTHERS.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemUserId(), BoundType.CLOSED, AccountConstants.getSystemUserId() + 1, BoundType.OPEN), SYSTEM_KINE_ACCOUNT.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemTradingFeeUserId(), BoundType.OPEN), SYSTEM_KINE_ACCOUNT_SUB.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemTradingFeeUserId(), BoundType.CLOSED, AccountConstants.getSystemTradingFeeUserId() + 1, BoundType.OPEN), SYSTEM_TRADING_FEE.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemTradingFeeUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemTradingFeeUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_TRADING_FEE_SUB.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemWithdrawFeeUserId(), BoundType.CLOSED, AccountConstants.getSystemWithdrawFeeUserId() + 1, BoundType.OPEN), SYSTEM_WITHDRAW_FEE.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemWithdrawFeeUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemWithdrawFeeUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_WITHDRAW_FEE_SUB.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemExchangeUserId(), BoundType.CLOSED, AccountConstants.getSystemExchangeUserId() + 1, BoundType.OPEN), SYSTEM_EXCHANGE.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemExchangeUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemExchangeUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_EXCHANGE_SUB.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemDividendChainUserId(), BoundType.CLOSED, AccountConstants.getSystemDividendChainUserId() + 1, BoundType.OPEN), SYSTEM_DIVIDEND_CHAIN.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemDividendChainUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemDividendChainUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_DIVIDEND_CHAIN_SUB.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemOperationUserId(), BoundType.CLOSED, AccountConstants.getSystemOperationUserId() + 1, BoundType.OPEN), SYSTEM_OPERATION.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemOperationUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemOperationUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_OPERATION_SUB.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemTradeUserId(), BoundType.CLOSED, AccountConstants.getSystemTradeUserId() + 1, BoundType.OPEN), SYSTEM_TRADE.getCode());
        accountTypeRm.put(Range.range(AccountConstants.getSystemTradeUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemTradeUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_TRADE_SUB.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemRebateUserId(), BoundType.CLOSED, AccountConstants.getSystemRebateUserId() + 1, BoundType.OPEN), SYSTEM_REBATE.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemRebateUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemRebateUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_REBATE_SUB.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemRebateUserId() + AccountConstants.getUseridReserveInterval(), BoundType.CLOSED, AccountConstants.getSystemLiquidateUserId(), BoundType.OPEN), OTHERS.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemLiquidateUserId(), BoundType.CLOSED, AccountConstants.getSystemLiquidateUserId() + 1, BoundType.OPEN), SYSTEM_LIQUIDATE.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemLiquidateUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemLiquidateUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_LIQUIDATE_SUB.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemFundingRateUserId(), BoundType.CLOSED, AccountConstants.getSystemFundingRateUserId() + 1, BoundType.OPEN), SYSTEM_FUNDING_RATE.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemFundingRateUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemFundingRateUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_FUNDING_RATE_SUB.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemKineRewardUserId(), BoundType.CLOSED, AccountConstants.getSystemKineRewardUserId() + 1, BoundType.OPEN), SYSTEM_REWARD_ACCOUNT.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemKineRewardUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemKineRewardUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_REWARD_ACCOUNT_SUB.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemMcdAdjustUserId(), BoundType.CLOSED, AccountConstants.getSystemMcdAdjustUserId() + 1, BoundType.OPEN), SYSTEM_MCD_ADJUST.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemMcdAdjustUserId() + 1, BoundType.CLOSED, AccountConstants.getSystemMcdAdjustUserId() + AccountConstants.getUseridReserveInterval(), BoundType.OPEN), SYSTEM_MCD_ADJUST_SUB.getCode());
//        accountTypeRm.put(Range.range(AccountConstants.getSystemMcdAdjustUserId() + AccountConstants.getUseridReserveInterval(), BoundType.CLOSED, 10000000L, BoundType.OPEN), OTHERS.getCode());

        sorterMap.put("default", Comparator.comparing(MgtAccountAssetDto::getAccountType));
//        sorterMap.put("{\"kusdBalance\":\"ascend\"}", Comparator.comparing(MgtAccountAssetDto::getKusdBalanceNum));
//        sorterMap.put("{\"kusdBalance\":\"descend\"}", Comparator.comparing(MgtAccountAssetDto::getKusdBalanceNum).reversed());
//        sorterMap.put("{\"kineBalance\":\"ascend\"}", Comparator.comparing(MgtAccountAssetDto::getKineBalanceNum));
//        sorterMap.put("{\"kineBalance\":\"descend\"}", Comparator.comparing(MgtAccountAssetDto::getKineBalanceNum).reversed());
        sorterMap.put("{\"usdtBalance\":\"ascend\"}", Comparator.comparing(MgtAccountAssetDto::getUsdtBalanceNum));
        sorterMap.put("{\"usdtBalance\":\"descend\"}", Comparator.comparing(MgtAccountAssetDto::getUsdtBalanceNum).reversed());
        sorterMap.put("{\"usdcBalance\":\"ascend\"}", Comparator.comparing(MgtAccountAssetDto::getUsdtBalanceNum));
        sorterMap.put("{\"usdcBalance\":\"descend\"}", Comparator.comparing(MgtAccountAssetDto::getUsdtBalanceNum).reversed());


        sorterHotWalletMap.put("default", Comparator.comparing(MgtHotWalletDto::getType));
//        sorterHotWalletMap.put("{\"kusdBalance\":\"ascend\"}", Comparator.comparing(MgtHotWalletDto::getKusdBalanceNum));
//        sorterHotWalletMap.put("{\"kusdBalance\":\"descend\"}", Comparator.comparing(MgtHotWalletDto::getKusdBalanceNum).reversed());
//        sorterHotWalletMap.put("{\"kineBalance\":\"ascend\"}", Comparator.comparing(MgtHotWalletDto::getKineBalanceNum));
//        sorterHotWalletMap.put("{\"kineBalance\":\"descend\"}", Comparator.comparing(MgtHotWalletDto::getKineBalanceNum).reversed());
        sorterHotWalletMap.put("{\"chainBalance\":\"ascend\"}", Comparator.comparing(MgtHotWalletDto::getChainBalanceNum));
        sorterHotWalletMap.put("{\"chainBalance\":\"descend\"}", Comparator.comparing(MgtHotWalletDto::getChainBalanceNum).reversed());
        sorterHotWalletMap.put("{\"usdtBalance\":\"ascend\"}", Comparator.comparing(MgtHotWalletDto::getUsdtBalanceNum));
        sorterHotWalletMap.put("{\"usdtBalance\":\"descend\"}", Comparator.comparing(MgtHotWalletDto::getUsdtBalanceNum).reversed());
        sorterHotWalletMap.put("{\"usdcBalance\":\"ascend\"}", Comparator.comparing(MgtHotWalletDto::getUsdtBalanceNum));
        sorterHotWalletMap.put("{\"usdcBalance\":\"descend\"}", Comparator.comparing(MgtHotWalletDto::getUsdtBalanceNum).reversed());

//        sorterDepositAddressMap.put(AccountConstants.QUOTE_CURRENCY, Comparator.comparing(MgtDepositAddressDto::getKusdBalanceNum).reversed());
        sorterDepositAddressMap.put(USDT, Comparator.comparing(MgtDepositAddressDto::getUsdtBalanceNum).reversed());
        sorterDepositAddressMap.put(NATIVE_TOKEN, Comparator.comparing(MgtDepositAddressDto::getChainBalanceNum).reversed());
//        sorterDepositAddressMap.put(AccountConstants.KINE, Comparator.comparing(MgtDepositAddressDto::getKineBalanceNum).reversed());
//        sorterDepositAddressMap.put(USDC, Comparator.comparing(MgtDepositAddressDto::getUsdcBalanceNum).reversed());
    }


    @Override
    public GenericDto<Page<MgtAddressCollectOrderHisDto>> getFundCollectOrderHistory(int chain, String currency, String address, int current, int pageSize, int type) {
        GenericDto<Page<AddressCollectOrderHisDto>> dto = accountFeignClient.getFundCollectOrderHistory(chain, 0l, 0l, type
                , address, currency, current, pageSize);
        if (!dto.isSuccess()) {
            return GenericDto.failure(dto.getMessage(), dto.getCode());
        }
        Page<MgtAddressCollectOrderHisDto> pageList = new Page<>();
        List<MgtAddressCollectOrderHisDto> mgtAddressCollectOrderHisDtos = assetManagementMapper.convertToMgtAddressCollectOrderHisDtos(dto.getData().getRecords());
        pageList.setRecords(mgtAddressCollectOrderHisDtos);
        pageList.setCurrent(dto.getData().getCurrent());
        pageList.setSize(dto.getData().getSize());
        pageList.setPages(dto.getData().getPages());
        pageList.setTotal(dto.getData().getTotal());

        return GenericDto.success(pageList);
    }


    @Override
    public GenericDto<List<MgtDepositAddressDto>> queryDepositAddress(String currency, int chain, String address, Integer thresholdAmount) {
        currency = isNotBlank(currency) ? currency : AccountConstants.USDT;
        GenericDto<List<AddressBalanceDto>> dto =
                accountFeignClient.getUserAddressBalances(chain, currency);
        if (!dto.isSuccess()) {
            return GenericDto.failure(dto.getMessage(), dto.getCode());
        }
        Map<String, AddressBalanceDto> balanceDtoMap =
                dto.getData().stream().collect(Collectors.toMap(AddressBalanceDto::getAddress, e -> e));
        List<MgtDepositAddressDto> dtos = Lists.newArrayList();
        if (isNotBlank(address) && !balanceDtoMap.containsKey(address)) {

        } else if (isNotBlank(address) && balanceDtoMap.containsKey(address)) {
            dtos.add(getMgtDepositAddressDto(balanceDtoMap.get(address)));
        } else {
            for (String key : balanceDtoMap.keySet()) {
                dtos.add(getMgtDepositAddressDto(balanceDtoMap.get(key)));
            }
        }
        // 根据输入的门槛金额过滤返回list
        if (!Objects.isNull(thresholdAmount) && thresholdAmount > 0) {
            dtos = dtos.stream().filter(p -> p.getUsdtBalanceNum().compareTo(new BigDecimal(thresholdAmount)) >= 0).collect(Collectors.toList());
        }
        if (getNativeTokens().contains(currency)) {
            currency = NATIVE_TOKEN;
        }
        dtos = dtos.stream().sorted(sorterDepositAddressMap.get(currency)).collect(Collectors.toList());
        return GenericDto.success(dtos);
    }
    @Override
    public GenericDto<Boolean> createOrder(MgtAddressCollectOrderRequestDto dto) {
        dto.setType(FundCollectOrderType.FROM_USER_TO_SYSTEM.getCode());
        GenericDto<AddressCollectOrderHisDto> addressDto =
                accountFeignClient.createFundCollectOrder(walletTransferRequestMapper.convert2AddressCollectOrderRequestDto(dto));
        if (!addressDto.isSuccess()) {
            return GenericDto.failure(addressDto.getMessage(), addressDto.getCode());
        }
        dto.setId(addressDto.getData().getId());
        return GenericDto.success(true);
    }

    @Override
    public GenericDto<Boolean> createGasFeeOrder(MgtAddressCollectOrderRequestDto dto) {
        dto.setType(FundCollectOrderType.FROM_SYSTEM_TO_USER.getCode());
        GenericDto<AddressCollectOrderHisDto> addressDto =
                accountFeignClient.createFundCollectOrder(walletTransferRequestMapper.convert2AddressCollectOrderRequestDto(dto));
        if (!addressDto.isSuccess()) {
            return GenericDto.failure(addressDto.getMessage(), addressDto.getCode());
        }
        dto.setId(addressDto.getData().getId());
        return GenericDto.success(true);
    }

    @Override
    public GenericDto<Boolean> createGasFeeAndCollectOrder(MgtGasFeeAndCollectOrderRequestDto requestDto) {

        // 获取预计gasFee = gasPrice(链上获取，没取到则读数据库配置) * gasLimit （数据库配置）
        Long gasFee = 0L;
        GenericDto<Long> respDto = accountFeignClient.getGasLimit(requestDto.getChain());
        if (!respDto.isSuccess()) {
            return GenericDto.failure(respDto.getCode(), respDto.getMessage());
        }
        GenericDto<ChainGasPriceDto> gasPrice = accountFeignClient.getGasPrice(requestDto.getChain());
        if (!gasPrice.isSuccess()) {
            return GenericDto.failure(gasPrice.getMessage(), gasPrice.getCode());
        }
        gasFee = respDto.getData() * gasPrice.getData().getProposeGasPrice();

        BigDecimal gasFeeDecimal;
        if (requestDto.getChain() == Chain.ETH.getCode()) {
            gasFeeDecimal = new BigDecimal(gasFee).divide(new BigDecimal(10).pow(18));

        } else {
            gasFeeDecimal = new BigDecimal(gasFee).divide(new BigDecimal(10).pow(6));

        }
        // 链上余额大于等于预估的gasfee,则可以直接发起归集操作
        List<MgtGasFeeAndCollectOrderRequestDto.MgtGasFeeAndCollectOrderDetail> collectList = requestDto.getList().stream()
                .filter(p -> new BigDecimal(p.getChainBalance()).compareTo(gasFeeDecimal) >= 0).collect(Collectors.toList());

        // 系统钱包地址
        String address = this.getSystemWalletAddress(requestDto.getChain());

        // 拼装数据
        if (!CollectionUtils.isEmpty(collectList)) {
            MgtAddressCollectOrderRequestDto dto = this.getMgtAddressCollectOrderRequestDto(requestDto);
            dto.setType(FundCollectOrderType.FROM_USER_TO_SYSTEM.getCode());
            dto.setCurrency(CurrencyEnum.USDT.getCode().toUpperCase());
            dto.setGasPrice(String.valueOf(gasPrice.getData().getProposeGasPrice()));

            dto.setAddress(address);
            List<MgtAddressCollectOrderRequestDto.MgtAddressOrderDetail> collect = collectList.stream().map(p -> {
                MgtAddressCollectOrderRequestDto.MgtAddressOrderDetail detail = new MgtAddressCollectOrderRequestDto.MgtAddressOrderDetail();
                detail.setAddress(p.getAddress());
                detail.setAmount(p.getUsdtBalance());
                return detail;
            }).collect(Collectors.toList());
            dto.setList(collect);

            // 发起归集
            GenericDto<AddressCollectOrderHisDto> collectOrder =
                    accountFeignClient.createFundCollectOrder(walletTransferRequestMapper.convert2AddressCollectOrderRequestDto(dto));
            if (!collectOrder.isSuccess()) {
                return GenericDto.failure(collectOrder.getMessage(), collectOrder.getCode());
            }
        }
        // 链上余额小于预估的gasfee,则需要先进行资金划转再进行归集操作
        List<MgtGasFeeAndCollectOrderRequestDto.MgtGasFeeAndCollectOrderDetail> transferGasFeeList = requestDto.getList().stream()
                .filter(p -> new BigDecimal(p.getChainBalance()).compareTo(gasFeeDecimal) < 0 && new BigDecimal(p.getUsdtBalance()).compareTo(new BigDecimal(0)) > 0)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(transferGasFeeList)) {
            // 拼装数据
            MgtAddressCollectOrderRequestDto gasFeedto = this.getMgtAddressCollectOrderRequestDto(requestDto);
            gasFeedto.setType(FundCollectOrderType.FROM_SYSTEM_TO_USER.getCode());
            gasFeedto.setGasPrice(String.valueOf(gasPrice.getData().getProposeGasPrice()));
            gasFeedto.setAddress(address);
            List<MgtAddressCollectOrderRequestDto.MgtAddressOrderDetail> gasFeeList = transferGasFeeList.stream().map(p -> {
                MgtAddressCollectOrderRequestDto.MgtAddressOrderDetail detail = new MgtAddressCollectOrderRequestDto.MgtAddressOrderDetail();
                detail.setAddress(p.getAddress());
                // 划转的金额为预估的手续费减去已有的金额
                detail.setAmount(String.valueOf(gasFeeDecimal.subtract(new BigDecimal(p.getChainBalance()))));
                return detail;
            }).collect(Collectors.toList());
            gasFeedto.setList(gasFeeList);

            // 发起gasFee划转
            GenericDto<AddressCollectOrderHisDto> transferOrder =
                    accountFeignClient.createFundCollectOrder(walletTransferRequestMapper.convert2AddressCollectOrderRequestDto(gasFeedto));
            if (!transferOrder.isSuccess()) {
                return GenericDto.failure(transferOrder.getMessage(), transferOrder.getCode());
            }
        }


        return GenericDto.success(true);
    }

    @Override
    public GenericDto<BalanceGetStatusDto> getBalanceGetStatus(int chain) {
        return accountFeignClient.getBalanceGetStatus(chain);
    }

    @Override
    public GenericDto<Boolean> createBalanceGet(int chain) {
        return accountFeignClient.createBalanceGet(chain);
    }

    @Override
    public GenericDto<List<MgtHotWalletDto>> queryHotWallets(Integer type, Integer chain, String address) {
        GenericDto<List<SystemWalletAddressDto>> dto = accountFeignClient.getAllSystemWalletAddress(chain);
        if (!dto.isSuccess()) {
            return GenericDto.failure(dto.getCode(), dto.getMessage());
        }

        GenericDto<List<AddressBalanceDto>> balancesDto = accountFeignClient.getSystemAddressBalances(chain);
        if (!balancesDto.isSuccess()) {
            return GenericDto.failure(balancesDto.getCode(), balancesDto.getMessage());
        }
        Map<String, AddressBalanceDto> balanceDtoMap =
                balancesDto.getData().stream().collect(Collectors.toMap(AddressBalanceDto::getAddress, e -> e, (v1,
                                                                                                                v2) -> v1));

        List<MgtHotWalletDto> mgtHotWalletDtos = Lists.newArrayList();
        for (SystemWalletAddressDto data : dto.getData().stream().filter(item -> {
            boolean result = true;
            if (type != null) {
                result = type.equals(item.getType());
            }
            if (isNotBlank(address)) {
                result = address.equals(item.getAddress());
            }
            return result && item.getChain() == chain;
        }).collect(Collectors.toList())) {
            String queryAddress = data.getAddress();
            Map<String, String> balances = Maps.newHashMap();
            AddressBalanceDto addressBalanceDto = balanceDtoMap.get(queryAddress);
            Map<String, BigDecimal> balancesMap = (addressBalanceDto != null) ? addressBalanceDto.getBalances() : Maps.newHashMap();
            String chainBalance = null;
            String usdtBalance = null;

            if (balancesMap != null && !balancesMap.isEmpty()) {
                for (String key : balancesMap.keySet()) {
                    String balance = balancesMap.get(key).setScale(6, BigDecimal.ROUND_DOWN).toPlainString();
                    balances.put(key, balance);
                    if (getNativeTokens().contains(key)) {
                        chainBalance = balance;
                    } else if (USDT.equalsIgnoreCase(key)) {
                        usdtBalance = balance;
                    }
                }
            }
            mgtHotWalletDtos.add(MgtHotWalletDto.builder()
                    .address(queryAddress)
                    .type(data.getType())
                    .chainBalance(chainBalance)
                    .usdtBalance(usdtBalance)
                    .balances(balances)
                    .chain(Chain.fromCode(data.getChain()).getName())
                    .comments(data.getComments())
                    .status(data.getStatus())
                    .tag(data.getTag())
                    .build());
        }
        return GenericDto.success(mgtHotWalletDtos);
    }

    @Override
    public GenericDto<Page<MgtWalletTransferDto>> queryWalletTransfers(MgtWalletTransferQueryDto queryDto) {
        GenericDto<Page<AddressCollectHisDto>> dto = accountFeignClient.getFundCollectHistory(
                queryDto.getChain(), queryDto.getStartTime(), queryDto.getEndTime(), queryDto.getFromAddress(), queryDto.getToAddress(), queryDto.getCurrent(), queryDto.getPageSize());
        if (!dto.isSuccess()) {
            return GenericDto.failure(dto.getMessage(), dto.getCode());
        }
        Page<MgtWalletTransferDto> pageList = new Page<>();

        pageList.setRecords(assetManagementMapper.convertToMgtWalletTransferDtos(dto.getData().getRecords().stream().filter(item -> {
            boolean result = true;
            if (isNotBlank(queryDto.getFromAddress())) {
                result = queryDto.getFromAddress().equals(item.getFromAddress());
            }
            if (isNotBlank(queryDto.getToAddress())) {
                result = queryDto.getToAddress().equals(item.getToAddress());
            }
            if (queryDto.getStatus() != null) {
                result = queryDto.getStatus().equals(item.getStatus());
            }
            return result;
        }).collect(Collectors.toList())));
        pageList.setCurrent(dto.getData().getCurrent());
        pageList.setSize(dto.getData().getSize());
        pageList.setPages(dto.getData().getPages());
        pageList.setTotal(dto.getData().getTotal());

        return GenericDto.success(pageList);
    }

    @Override
    public GenericDto<String> walletTransfer(MgtWalletTransferRequestDto requestDto) {
        GenericDto<AddressCollectHisDto> dto =
                accountFeignClient.createFundCollect(walletTransferRequestMapper.convert2FundCollectRequestDto(requestDto));
        if (!dto.isSuccess()) {
            return GenericDto.failure(dto.getMessage(), dto.getCode());
        }
        requestDto.setId(dto.getData().getId());
        return GenericDto.success(dto.getData().getFromAddress());
    }

    @Override
    public GenericDto<MgtGasConfig> getGasConfig(int chain) {
        GenericDto<Long> respDto = accountFeignClient.getGasLimit(chain);
        if (!respDto.isSuccess()) {
            return GenericDto.failure(respDto.getCode(), respDto.getMessage());
        }
        GenericDto<ChainGasPriceDto> gasPrice = accountFeignClient.getGasPrice(chain);
        if (!gasPrice.isSuccess()) {
            return GenericDto.failure(gasPrice.getMessage(), gasPrice.getCode());
        }
        MgtGasConfig config = new MgtGasConfig();
        config.setGasLimit(respDto.getData());
        config.setGasPrice(gasPrice.getData().getProposeGasPrice());
        config.setGasFee(config.getGasLimit() * config.getGasPrice());
        return GenericDto.success(config);
    }

    @Override
    public GenericDto<Map<Chain, Map<String, BigDecimal>>> getPendingCollectBalances() throws Exception {
        return accountFeignClient.getPendingCollectBalances();
    }

    @Override
    public GenericDto<ChainGasPriceDto> getGasPrice(int chain) {
        return accountFeignClient.getGasPrice(chain);
    }

    @Override
    public GenericDto<List<AddressCollectHisDto>> getAddressCollectByOrderId(long orderId) {
        return accountFeignClient.getFundCollectHistoryByOrder(orderId);
    }

    @Override
    @AuditLog(module = Module.CASH_MANAGEMENT, subModule = SubModule.WALLET_ACCOUNT, action = Action.ADD)
    public GenericDto<Boolean> createHotWallet(MgtSystemWalletAddressDto dto) {
        GenericDto<List<SystemWalletAddressDto>> systemWalletAddressDtoList = accountFeignClient.getAllSystemWalletAddress(dto.getChain());
        if (!systemWalletAddressDtoList.isSuccess()) {
            return GenericDto.failure(systemWalletAddressDtoList.getCode(), systemWalletAddressDtoList.getMessage());
        }
        List<SystemWalletAddressDto> walletList = systemWalletAddressDtoList.getData();
        if (CollUtil.isNotEmpty(walletList)) {
            // 判断每条链只能有一个热钱包
            throw new AccountException(ErrorCode.SYSTEM_WALLET_ADDRESS_REPEAT);
        }

        GenericDto<SystemWalletAddressDto> addressDto = accountFeignClient.createSystemWalletAddress(dto.getChain());
        if (!addressDto.isSuccess()) {
            return GenericDto.failure(addressDto.getMessage(), addressDto.getCode());
        }
        SystemWalletAddressDto data = addressDto.getData();
        BeanCopy.beans(data, dto).copy();
        return GenericDto.success(true);
    }

    private MgtDepositAddressDto getMgtDepositAddressDto(AddressBalanceDto addressBalanceDto) {
        Map<String, BigDecimal> balancesMap = addressBalanceDto.getBalances();
        MgtDepositAddressDto addressDto = MgtDepositAddressDto.builder().address(addressBalanceDto.getAddress()).build();
        addressDto.setEmail(addressBalanceDto.getEmail());
        if (balancesMap != null && !balancesMap.isEmpty()) {
            for (String key : balancesMap.keySet()) {
                String balance = balancesMap.get(key).setScale(8, BigDecimal.ROUND_DOWN).toPlainString();
                if (AccountConstants.KUSD_CURRENCY.equalsIgnoreCase(key)) {
                    // addressDto.setKusdBalance(balance);
                } else if (USDT.equalsIgnoreCase(key)) {
                    addressDto.setUsdtBalance(balance);
                } else if (getNativeTokens().contains(key)) {
                    addressDto.setChainBalance(balance);
                } else if (AccountConstants.KINE.equalsIgnoreCase(key)) {
                    // addressDto.setKineBalance(balance);
                } else if (USDC.equalsIgnoreCase(key)) {
                    //  addressDto.setUsdcBalance(balance);
                }
            }
        }
        return addressDto;
    }

    public static Set<String> getNativeTokens() {
        Set<String> tokens = Sets.newHashSet();
        for (Chain chain : Chain.values()) {
            tokens.add(chain.getNativeToken());
        }
        return tokens;
    }

    private MgtAddressCollectOrderRequestDto getMgtAddressCollectOrderRequestDto(MgtGasFeeAndCollectOrderRequestDto requestDto) {
        MgtAddressCollectOrderRequestDto dto = new MgtAddressCollectOrderRequestDto();
        dto.setChain(requestDto.getChain());
        dto.setCurrency(Chain.fromCode(requestDto.getChain()).getNativeToken());
        return dto;
    }

    // 获取chain 对应的系统钱包地址，可修改为读取数据库配置
    private String getSystemWalletAddress(int chain) {
        String address = "";
        List<SystemWalletAddressDto> data = accountFeignClient.getAllSystemWalletAddress(chain).getData();
        if (!CollectionUtils.isEmpty(data)) {
            address = data.get(0).getAddress();
        }
        return address;
    }
}
