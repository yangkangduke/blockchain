package com.seeds.uc.feign;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.dto.request.VerifyAuthTokenReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.service.ApiKey;

import java.util.List;

/**
 * @author raowentong
 * @email antilaw@yahoo.com
 * @date 2020/8/26
 */
@FeignClient(name = "userCenterFeignClient", url = "${service.url.uc}")
public interface UserCenterFeignClient {

    @PostMapping("/uc-internal/token/verify")
    GenericDto<Boolean> verifyToken(@RequestBody VerifyAuthTokenReq verifyAuthTokenReq);
    @PostMapping("/uc-internal/user/metamask/verify-signature")
    GenericDto<Boolean> metaMaskVerifySignature(@RequestBody MetamaskVerifyReq metamaskReq);
}
