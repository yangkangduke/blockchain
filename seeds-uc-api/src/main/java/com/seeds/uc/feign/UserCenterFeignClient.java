package com.seeds.uc.feign;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.dto.request.VerifyAuthTokenReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据用户ids获取邮箱
     *
     * @param ids
     * @return
     */
    @PostMapping("/uc-internal/user/get-email-by-ids")
    GenericDto<Map<Long, String>> getEmailByIds(@RequestBody List<Long> ids);

}
