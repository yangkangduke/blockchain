
package com.seeds.uc.feign;


import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.LoginReq;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.dto.response.UserInfoResp;
import com.seeds.uc.feign.interceptor.FeignInnerRequestInterceptor;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author yk
 * @date 2022/8/8
 */
@FeignClient(name = "remoteUserService", url = "127.0.0.1:10101", configuration = {FeignInnerRequestInterceptor.class})
public interface RemoteNFTService {


	/**
	 * 获取用户信息
	 * @return
	 */
	@GetMapping("/info")
	@ApiOperation(value = "获取用户信息", notes = "获取用户信息")
	GenericDto<UserInfoResp> getInfo(HttpServletRequest request);



}
