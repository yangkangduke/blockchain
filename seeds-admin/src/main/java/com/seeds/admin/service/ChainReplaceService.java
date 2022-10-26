package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.ChainTxnReplayDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.common.dto.GenericDto;

public interface ChainReplaceService {

    GenericDto<Boolean> executeWithdraw(ChainTxnReplayDto dto);

    GenericDto<Boolean> executeWalletTransfer(ChainTxnReplayDto dto);

    GenericDto<Boolean> executeCreateOrder(ChainTxnReplayDto dto);

    GenericDto<Boolean> executeCreateGasFeeOrder(ChainTxnReplayDto dto);

    GenericDto<Page<ChainTxnDto>> getChainTxnList(ChainTxnPageReq req);

}
