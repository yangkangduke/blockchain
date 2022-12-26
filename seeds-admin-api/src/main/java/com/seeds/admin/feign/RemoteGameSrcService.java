package com.seeds.admin.feign;

import com.seeds.admin.dto.response.GameSrcLinkResp;
import com.seeds.admin.feign.interceptor.AdminFeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @author hewei
 * @date 2022/12/22
 */
@FeignClient(name = "remoteGameSrcService", url = "${service.url.admin}", configuration = {AdminFeignInnerRequestInterceptor.class})
public interface RemoteGameSrcService {


	@GetMapping("/internal-game-resource/get-links")
	@ApiOperation("获取需要请求的链接地址")
	GenericDto<List<GameSrcLinkResp>> getLinks(@RequestParam(value = "ip") String ip,
											   @RequestParam(value = "type") Integer type);


}
