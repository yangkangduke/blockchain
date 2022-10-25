package com.seeds.account.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.ChainTxnReplayDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.dto.ApproveRejectDto;
import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.common.dto.GenericDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

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
}
