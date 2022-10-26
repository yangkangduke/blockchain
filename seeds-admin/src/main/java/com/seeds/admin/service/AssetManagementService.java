package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.BalanceGetStatusDto;
import com.seeds.admin.dto.*;
import com.seeds.common.dto.GenericDto;

/**
 * @author: hewei
 * @date 2022/10/26
 */
public interface AssetManagementService {

    GenericDto<Page<MgtAddressCollectOrderHisDto>> getFundCollectOrderHistory(int chain, String currency, String address, int current, int pageSize, int type);

    GenericDto<Boolean> createGasFeeOrder(MgtAddressCollectOrderRequestDto dto);

    GenericDto<Boolean> createOrder(MgtAddressCollectOrderRequestDto dto);

    GenericDto<Page<MgtDepositAddressDto>> queryDepositAddress(String currency, int chain, String address, String sorter);

    GenericDto<BalanceGetStatusDto> getBalanceGetStatus(int chain);

    GenericDto<Boolean> createBalanceGet(int chain);

    GenericDto<Page<MgtHotWalletDto>> queryHotWallets(Integer type, int chain, String address, String sorter);

    GenericDto<Page<MgtWalletTransferDto>> queryWalletTransfers(MgtWalletTransferQueryDto queryDto);

    GenericDto<String> walletTransfer(MgtWalletTransferRequestDto requestDto);

    GenericDto<MgtGasConfig> getGasConfig(int chain);

}
