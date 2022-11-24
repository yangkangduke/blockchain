package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.AddressCollectHisDto;
import com.seeds.account.dto.BalanceGetStatusDto;
import com.seeds.account.dto.ChainGasPriceDto;
import com.seeds.admin.dto.*;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: hewei
 * @date 2022/10/26
 */
public interface AssetManagementService {

    GenericDto<Page<MgtAddressCollectOrderHisDto>> getFundCollectOrderHistory(int chain, String currency, String address, int current, int pageSize, int type);

    GenericDto<Boolean> createGasFeeOrder(MgtAddressCollectOrderRequestDto dto);

    GenericDto<Boolean> createOrder(MgtAddressCollectOrderRequestDto dto);

    GenericDto<List<MgtDepositAddressDto>> queryDepositAddress(String currency, int chain, String address,Integer thresholdAmount);

    GenericDto<BalanceGetStatusDto> getBalanceGetStatus(int chain);

    GenericDto<Boolean> createBalanceGet(int chain);

    GenericDto<List<MgtHotWalletDto>> queryHotWallets(Integer type, Integer chain, String address);

    GenericDto<Page<MgtWalletTransferDto>> queryWalletTransfers(MgtWalletTransferQueryDto queryDto);

    GenericDto<String> walletTransfer(MgtWalletTransferRequestDto requestDto);

    GenericDto<MgtGasConfig> getGasConfig(int chain);

    GenericDto<Map<Chain, Map<String, BigDecimal>>> getPendingCollectBalances() throws Exception;

    GenericDto<ChainGasPriceDto> getGasPrice(int chain);

    GenericDto<List<AddressCollectHisDto>> getAddressCollectByOrderId(long orderId);

    GenericDto<Boolean> createHotWallet(MgtSystemWalletAddressDto dto);
}
