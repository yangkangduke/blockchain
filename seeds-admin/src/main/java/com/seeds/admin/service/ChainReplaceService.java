package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.admin.dto.MgtChainTxnReplaceDto;
import com.seeds.admin.dto.MgtChainTxnReplayDto;
import com.seeds.admin.dto.MgtOriginOrderReplaceDto;
import com.seeds.common.dto.GenericDto;


public interface ChainReplaceService {

    GenericDto<Boolean> executeWithdraw(MgtChainTxnReplayDto chainTxnReplayDto);

    GenericDto<Boolean> executeWalletTransfer(MgtChainTxnReplayDto chainTxnReplayDto);

    GenericDto<Boolean> executeCreateOrder(MgtChainTxnReplayDto chainTxnReplayDto);

    GenericDto<Boolean> executeCreateGasFeeOrder(MgtChainTxnReplayDto chainTxnReplayDto);

    GenericDto<Page<ChainTxnDto>> getChainTxnList(ChainTxnPageReq req);

    void recordOriginReplaceOrder(MgtOriginOrderReplaceDto originOrderReplaceDto);

    GenericDto<Boolean> executeChainReplacement(MgtChainTxnReplaceDto chainTxnReplaceDto);

    GenericDto<Page<ChainTxnDto>> getChainTxnReplaceList(ChainTxnPageReq req);

}
