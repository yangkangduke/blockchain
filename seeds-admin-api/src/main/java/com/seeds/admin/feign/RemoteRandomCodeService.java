package com.seeds.admin.feign;

import com.seeds.admin.dto.request.*;
import com.seeds.admin.feign.impl.RemoteRandomCodeServiceImpl;
import com.seeds.admin.feign.interceptor.AdminFeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * @author hang.yu
 * @date 2022/8/19
 */
@FeignClient(name = "remoteRandomCodeService", url = "${Seeds-admin}", fallback = RemoteRandomCodeServiceImpl.class, configuration = {AdminFeignInnerRequestInterceptor.class})
public interface RemoteRandomCodeService {

	/**
	 * 使用随机码
	 * @param req 随机码信息
	 * @return Object
	 */
	@PostMapping("/internal-random-code/use-random-code")
	@ApiOperation(value = "使用随机码", notes = "使用随机码")
	GenericDto<Object> useRandomCode(@Valid @RequestBody RandomCodeUseReq req);

}
