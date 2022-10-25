package com.seeds.account.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.dto.PagedDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
     * 获取需要审核的充提
     *
     * @return
     */
    @PostMapping("/mgt/pending-transaction")
    GenericDto<Page<ChainDepositWithdrawHisDto>> getPendingTransactions(@RequestBody AccountPendingTransactionsReq transactionsReq);

}
