
package com.seeds.uc.feign;


import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.LoginReq;
import com.seeds.uc.dto.request.NFTBuyReq;
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
@FeignClient(name = "remoteNFTService", url = "127.0.0.1:10101", configuration = {FeignInnerRequestInterceptor.class})
public interface RemoteNFTService {

	@GetMapping("/nft/buy/callback")
	@ApiOperation(value = "购买回调接口", notes = "购买回调接口")
	GenericDto<Object> buyNFTCallback(@Valid @RequestBody NFTBuyReq buyReq);



}
