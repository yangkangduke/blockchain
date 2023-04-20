package com.seeds.uc.feign;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.AllUserReq;
import com.seeds.uc.dto.request.MetamaskVerifyReq;
import com.seeds.uc.dto.request.VerifyAuthTokenReq;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.dto.response.UserInfoResp;
import com.seeds.uc.dto.response.UserRegistrationResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 根据用户id获取用户地址
     *
     * @param id 用户编号
     * @return 用户地址
     */
    @GetMapping("/uc-internal/user/get-public-address")
    GenericDto<String> getPublicAddress(@RequestParam Long id);

    /**
     * 根据用户钱包地址获取用户信息
     *
     * @param publicAddress 钱包地址
     * @return 用户信息
     */
    @GetMapping("/uc-internal/user/get-by-public-address")
    GenericDto<UcUserResp> getByPublicAddress(@RequestParam String publicAddress);
}
