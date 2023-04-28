package com.seeds.admin.feign;

import com.seeds.admin.feign.impl.RemoteKolServiceImpl;
import com.seeds.admin.feign.interceptor.AdminFeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author hewei
 * @date 2022/12/22
 */
@FeignClient(name = "remoteKolService", url = "${service.url.admin}", fallback = RemoteKolServiceImpl.class, configuration = {AdminFeignInnerRequestInterceptor.class})
public interface RemoteKolService {


	@GetMapping("/internal-kol/invite-code")
	@ApiOperation("获取邀请码")
	GenericDto<String> inviteCode(@RequestParam String inviteNo);

}
