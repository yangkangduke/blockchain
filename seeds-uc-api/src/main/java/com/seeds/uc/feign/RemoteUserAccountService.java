
package com.seeds.uc.feign;


import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.response.UcUserAccountAmountResp;
import com.seeds.uc.feign.interceptor.UcFeignInnerRequestInterceptor;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hang.yu
 * @date 2022/10/11
 */
@FeignClient(name = "remoteUserAccountService", url = "${Seeds-uc}", configuration = {UcFeignInnerRequestInterceptor.class})
public interface RemoteUserAccountService {

	@GetMapping("/internal-account/amount-info")
	@ApiOperation(value = "账户金额详情", notes = "账户金额详情")
	GenericDto<List<UcUserAccountAmountResp>> amountInfo(@RequestParam Long userId);

}
