package com.seeds.account.feign;

import com.seeds.common.dto.GenericDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "seeds-account", url = "${service.url.account}",path = "/account-internal", configuration = AccountFeignConfig.class)
public interface AccountFeignClient {

    /**
     * 负责创建空闲地址
     *
     * @return
     */
    @PostMapping("/job/scan-and-create-addresses")
    GenericDto<Boolean> scanAndCreateAddresses();


}
