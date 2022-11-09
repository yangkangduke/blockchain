package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.*;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.*;
import com.seeds.account.enums.FundCollectOrderType;
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
import jodd.bean.BeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public GenericDto<Page<MgtDepositAddressDto>> queryDepositAddress(String currency, int chain, String address, Integer thresholdAmount) {
        currency = isNotBlank(currency) ? currency : AccountConstants.USDT;
        GenericDto<List<AddressBalanceDto>> dto =
                accountFeignClient.getUserAddressBalances(chain, currency);
        if (!dto.isSuccess()) return GenericDto.failure(dto.getMessage(), dto.getCode());
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
        Page<MgtDepositAddressDto> page = new Page<>();
        page.setRecords(dtos.stream().sorted(sorterDepositAddressMap.get(currency)).collect(Collectors.toList()));
        return GenericDto.success(page);
    }
    @Override
    public GenericDto<Boolean> createOrder(MgtAddressCollectOrderRequestDto dto) {
        dto.setType(FundCollectOrderType.FROM_USER_TO_SYSTEM.getCode());
        GenericDto<AddressCollectOrderHisDto> addressDto =
                accountFeignClient.createFundCollectOrder(walletTransferRequestMapper.convert2AddressCollectOrderRequestDto(dto));
        if (!addressDto.isSuccess()) return GenericDto.failure(addressDto.getMessage(), addressDto.getCode());
        dto.setId(addressDto.getData().getId());
        return GenericDto.success(true);
    }
    @Override
    public GenericDto<Boolean> createGasFeeOrder(MgtAddressCollectOrderRequestDto dto) {
        dto.setType(FundCollectOrderType.FROM_SYSTEM_TO_USER.getCode());
        GenericDto<AddressCollectOrderHisDto> addressDto =
                accountFeignClient.createFundCollectOrder(walletTransferRequestMapper.convert2AddressCollectOrderRequestDto(dto));
        if (!addressDto.isSuccess()) return GenericDto.failure(addressDto.getMessage(), addressDto.getCode());
        dto.setId(addressDto.getData().getId());
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
    public GenericDto<Page<MgtHotWalletDto>> queryHotWallets(Integer type, int chain, String address) {
        GenericDto<List<SystemWalletAddressDto>> dto = accountFeignClient.getAllSystemWalletAddress(chain);
        if (!dto.isSuccess()) return GenericDto.failure(dto.getCode(), dto.getMessage());

        GenericDto<List<AddressBalanceDto>> balancesDto = accountFeignClient.getSystemAddressBalances(chain);
        if (!balancesDto.isSuccess()) return GenericDto.failure(balancesDto.getCode(), balancesDto.getMessage());
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
            String kusdBalance = null;
            String kineBalance = null;
            String chainBalance = null;
            String usdtBalance = null;
            String usdcBalance = null;

            if (balancesMap != null && !balancesMap.isEmpty()) {
                for (String key : balancesMap.keySet()) {
                    String balance = balancesMap.get(key).setScale(6, BigDecimal.ROUND_DOWN).toPlainString();
                    balances.put(key, balance);
                    if (AccountConstants.KUSD_CURRENCY.equalsIgnoreCase(key)) {
                        kusdBalance = balance;
                    } else if (KINE.equalsIgnoreCase(key)) {
                        kineBalance = balance;
                    } else if (getNativeTokens().contains(key)) {
                        chainBalance = balance;
                    } else if (USDT.equalsIgnoreCase(key)) {
                        usdtBalance = balance;
                    } else if (USDC.equalsIgnoreCase(key)) {
                        usdcBalance = balance;
                    }
                }
            }
            mgtHotWalletDtos.add(MgtHotWalletDto.builder()
                    .address(queryAddress)
                    .type(data.getType())
                    //.kusdBalance(kusdBalance)
                    //.kineBalance(kineBalance)
                    .chainBalance(chainBalance)
                    .usdtBalance(usdtBalance)
                    //.usdcBalance(usdcBalance)
                    .balances(balances)
                    .chain(Chain.fromCode(data.getChain()).getName())
                    .build());
        }
//        if (isBlank(sorter) || "{}".equals(sorter)) {
//            sorter = "default";
//        }
        Page<MgtHotWalletDto> page = new Page<>();
//        page.setRecords(mgtHotWalletDtos.stream().sorted(sorterHotWalletMap.get(sorter)).collect(Collectors.toList()));
        page.setRecords(mgtHotWalletDtos.stream().collect(Collectors.toList()));
        return GenericDto.success(page);
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
        if (!dto.isSuccess()) return GenericDto.failure(dto.getMessage(), dto.getCode());
        requestDto.setId(dto.getData().getId());
        return GenericDto.success(dto.getData().getFromAddress());
    }

    @Override
    public GenericDto<MgtGasConfig> getGasConfig(int chain) {
        GenericDto<Long> respDto = accountFeignClient.getGasLimit(chain);
        if (!respDto.isSuccess()) return GenericDto.failure(respDto.getCode(), respDto.getMessage());
        GenericDto<ChainGasPriceDto> gasPrice = accountFeignClient.getGasPrice(chain);
        if (!gasPrice.isSuccess()) return GenericDto.failure(gasPrice.getMessage(), gasPrice.getCode());
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
        GenericDto<SystemWalletAddressDto> addressDto = accountFeignClient.createSystemWalletAddress(dto.getChain());
        if (!addressDto.isSuccess()) return GenericDto.failure(addressDto.getMessage(), addressDto.getCode());
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
}
