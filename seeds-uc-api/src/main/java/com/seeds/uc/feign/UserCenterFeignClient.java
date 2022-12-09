package com.seeds.uc.feign;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.AllUserReq;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.dto.request.VerifyAuthTokenReq;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.dto.response.UserRegistrationResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/uc-internal/user/all-user")
    @ApiOperation("获取所有用户信息")
    GenericDto<com.baomidou.mybatisplus.extension.plugins.pagination.Page<UcUserResp>> getAllUser(@RequestBody AllUserReq allUserReq);

    @PostMapping("/uc-internal/user/user-list")
    @ApiOperation("获取用户信息列表")
    GenericDto<List<UcUserResp>> getUserList(@RequestBody List<Long> ids);

    /**
     * 获取用户注册情况 总注册用户数和今日新增注册用户数
     *
     * @return
     */
    @GetMapping("uc-internal/user/user-registration")
    GenericDto<UserRegistrationResp> getUserRegistration();
}
