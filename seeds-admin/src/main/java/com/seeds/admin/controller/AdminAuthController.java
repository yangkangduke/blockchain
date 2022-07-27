package com.seeds.admin.controller;

import com.seeds.admin.constant.AdminRedisKeys;
import com.seeds.admin.dto.redis.LoginAdminUser;
import com.seeds.admin.dto.request.AdminLoginReq;
import com.seeds.admin.dto.response.AdminLoginResp;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.SysAuthTypeEnum;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.service.AdminCacheService;
import com.seeds.admin.service.AdminCaptchaService;
import com.seeds.admin.service.SysUserService;
import com.seeds.admin.utils.HashUtil;
import com.seeds.admin.utils.RandomUtil;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.HttpHeaders;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 登录认证
 * @author hang.yu
 * @date 2022/7/13
 */
@Slf4j
@Api("登录认证")
@RestController
@RequestMapping("/auth")
public class AdminAuthController {

    @Autowired
    private AdminCaptchaService adminCaptchaService;

    @Autowired
    private AdminCacheService adminCacheService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/captcha/{account}")
    @ApiOperation(value = "生成图形验证码")
    public void captcha(HttpServletResponse response, @PathVariable("account") String account) throws IOException {
        // 账号不能为空
        if (StringUtils.isEmpty(account)) {
            throw new GenericException("Account cannot be empty");
        }
        //生成验证码
        adminCaptchaService.createCaptcha(response, account);
    }


    /**
     * 1. 根据authType判断登录方式
     * 2. 图形验证码验证/sms opt 验证
     * 3. 验证用户信息：密码、账号状态
     * 3. 登录成功/失败
     * 4. 返回token
     * @param loginReq 登录信息
     * @return token相关
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public GenericDto<AdminLoginResp> login(@Valid @RequestBody AdminLoginReq loginReq) {
        String authType = loginReq.getAuthType();
        SysUserEntity adminUser;
        if (SysAuthTypeEnum.PHONE.getCode().equals(authType)) {
            // 手机号登录
            if (StringUtils.isEmpty(loginReq.getMobile())) {
                return GenericDto.failure(AdminErrorCodeEnum.ERR_504_MISSING_ARGUMENTS.getDescEn(), AdminErrorCodeEnum.ERR_504_MISSING_ARGUMENTS.getCode(), null);
            }
            // 验证opt
            boolean flag = adminCaptchaService.validateSmsCaptcha(loginReq.getMobile(), loginReq.getOpt());
            if (!flag) {
                return GenericDto.failure(AdminErrorCodeEnum.ERR_10032_WRONG_SMS_CODE.getDescEn(), AdminErrorCodeEnum.ERR_10032_WRONG_SMS_CODE.getCode(), null);
            }
            adminUser = sysUserService.queryByMobile(loginReq.getMobile());
        } else if (SysAuthTypeEnum.PASSWORD.getCode().equals(authType)) {
            // 账号密码登录
            if (StringUtils.isEmpty(loginReq.getAccount()) || StringUtils.isEmpty(loginReq.getPassword())) {
                return GenericDto.failure(AdminErrorCodeEnum.ERR_504_MISSING_ARGUMENTS.getDescEn(), AdminErrorCodeEnum.ERR_504_MISSING_ARGUMENTS.getCode(), null);
            }
            // 验证码是否正确
            boolean flag = adminCaptchaService.validateCaptcha(loginReq.getAccount(), loginReq.getOpt());
            if (!flag) {
                return GenericDto.failure(AdminErrorCodeEnum.ERR_10039_WRONG_GRAPHIC_AUTH_CODE.getDescEn(), AdminErrorCodeEnum.ERR_10039_WRONG_GRAPHIC_AUTH_CODE.getCode(), null);
            }
            adminUser = sysUserService.queryByAccount(loginReq.getAccount());
        } else {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn(), AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getCode(), null);
        }
        // 用户不存在
        if (adminUser == null) {
            return GenericDto.failure(AdminErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED.getDescEn(), AdminErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED.getCode(), null);
        }
        // 密码错误
        if (StringUtils.isNotBlank(loginReq.getPassword())) {
            String loginPassword = HashUtil.sha256(loginReq.getPassword() + adminUser.getSalt());
            if (!loginPassword.equals(adminUser.getPassword())) {
                return GenericDto.failure(AdminErrorCodeEnum.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT.getDescEn(), AdminErrorCodeEnum.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT.getCode(), null);
            }
        }
        // 账号停用
        if(SysStatusEnum.DISABLE.value() == adminUser.getStatus()){
            return GenericDto.failure(AdminErrorCodeEnum.ERR_10014_ACCOUNT_FROZEN.getDescEn(), AdminErrorCodeEnum.ERR_10014_ACCOUNT_FROZEN.getCode(), null);
        }
        // 下发token
        String token = RandomUtil.genRandomToken(AdminRedisKeys.ADMIN_KEY_PREFIX + adminUser.getId().toString());
        // 登录成功，将token存入redis
        Integer expire = adminCacheService.putAdminUserWithToken(token, adminUser.getId());
        return GenericDto.success(AdminLoginResp.builder()
                .token(token)
                .expire(expire)
                .build());
    }

    @GetMapping("login/check")
    @ApiOperation(value = "登录状态检查")
    public GenericDto<Boolean> getLoginState(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.ADMIN_USER_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            LoginAdminUser adminUser = adminCacheService.getAdminUserByToken(token);
            if (adminUser != null && adminUser.getExpireAt() > System.currentTimeMillis()) {
                return GenericDto.success(true);
            }
        }
        return GenericDto.success(false);
    }

    @PostMapping("logout")
    @ApiOperation(value = "退出登录")
    public GenericDto<Object> logout(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.ADMIN_USER_TOKEN);
        adminCacheService.removeAdminUserByToken(token);
        return GenericDto.success(null);
    }

}