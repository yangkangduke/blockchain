package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.constant.UcConstants;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.AuthCode;
import com.seeds.uc.dto.redis.AuthToken;
import com.seeds.uc.dto.redis.LoginUser;
import com.seeds.uc.dto.redis.SecurityAuth;
import com.seeds.uc.dto.request.security.item.*;
import com.seeds.uc.dto.request.security.item.EmailSecurityItemReq;
import com.seeds.uc.dto.request.security.item.GaSecurityItemReq;
import com.seeds.uc.dto.request.security.item.PhoneSecurityItemReq;
import com.seeds.uc.dto.request.security.item.RebindGaReq;
import com.seeds.uc.dto.request.security.item.RebindPhoneReq;
import com.seeds.uc.dto.response.GoogleAuthResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.exceptions.SecurityItemException;
import com.seeds.uc.exceptions.SecuritySettingException;
import com.seeds.uc.mapper.SecurityStrategyMapper;
import com.seeds.uc.mapper.UserMapper;
import com.seeds.uc.model.SecurityStrategy;
import com.seeds.uc.model.User;
import com.seeds.uc.service.CacheService;
import com.seeds.uc.service.GoogleAuthService;
import com.seeds.uc.service.UserService;
import com.seeds.uc.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/25
 */

/**
 * 安全项相关接口，绑定解绑换绑等
 */
@Slf4j
@RestController
@RequestMapping("/uc/security/item")
public class OpenSecurityItemController {
    @Autowired
    CacheService cacheService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SecurityStrategyMapper securityStrategyMapper;

    @Autowired
    GoogleAuthService googleAuthService;

    @Autowired
    UserService userService;

    /**
     * 调用流程
     * 调用方法/uc/open/sms_code/send, 参数use_type=BIND_PHONE, 发送短信验证码
     * 调用方法/uc/open/sms_code/verify, 参数use_type=BIND_PHONE, 进行手机的验证, 获取到phone_token.
     * 调用方法/uc/open/email_code/send, 参数use_type=VERIFY_SETTING_POLICY_BIND_PHONE, 发送邮箱验证码.[根据安全设置判断是否执行]
     * 调用方法/uc/open/security/strategy/verify, 参数use_type=VERIFY_SETTING_POLICY_BIND_PHONE, 进行安全验证, 获取到auth_token.
     * 调用/uc/open/phone/bind, 不使用email_code, ga_code参数.
     */
    @PostMapping("phone/bind")
    public GenericDto<Object> bindPhone(@RequestBody PhoneSecurityItemReq securityItemReq) {
        AuthToken phoneAuthToken =
                cacheService.getAuthTokenDetailWithToken(securityItemReq.getPhoneToken(), ClientAuthTypeEnum.PHONE);
        if (phoneAuthToken == null
                || StringUtils.isBlank(phoneAuthToken.getAccountName())) {
            throw new SecurityItemException(UcErrorCode.ERR_10032_WRONG_SMS_CODE);
        }
        SecurityAuth securityAuth =
                cacheService.getSecurityAuthWithToken(securityItemReq.getAuthToken());
        if (securityAuth == null) {
            throw new SecuritySettingException(UcErrorCode.ERR_10210_SECURITY_VERIFY_ERROR);
        }
        String accountName = phoneAuthToken.getAccountName();
        Long uid = UserContext.getCurrentUserId();
        // 检查account name 是否已经存在
        User checkUser = userMapper.selectByPhone(accountName);
        if (checkUser != null) {
            throw new SecurityItemException(UcErrorCode.ERR_10051_PHONE_ALREADY_BEEN_USED);
        }
        User userToUpdate = userMapper.selectByPrimaryKey(uid);
        userToUpdate.setPhone(accountName);
        userToUpdate.setCountryCode(securityItemReq.getCountryCode());
        userMapper.updateByPrimaryKey(userToUpdate);

        // 绑定之后set 安全项
        securityStrategyMapper.insert(
                SecurityStrategy.builder()
                        .authType(ClientAuthTypeEnum.PHONE)
                        .createdAt(System.currentTimeMillis())
                        .needAuth(true)
                        .uid(uid)
                        .updatedAt(System.currentTimeMillis())
                        .build());
        return GenericDto.success(null);
    }

    /**
     * 调用方法/uc/open/sms_code/send, 使用新手机，参数use_type=REBIND_PHONE, 发送短信验证码
     * 调用方法/uc/open/sms_code/verify, 使用新手机，参数use_type=REBIND_PHONE, 进行手机的验证, 获取到new_phone_token.
     * 调用方法/uc/open/sms_code/send, 使用原手机，参数use_type=VERIFY_SETTING_POLICY_REBIND_PHONE, 发送手机验证码(因为要更换手机, 则必须验证手机, 不管二次验证是否开启手机验证).
     * 调用方法/uc/open/email_code/send, 参数use_type=VERIFY_SETTING_POLICY_REBIND_PHONE, 发送邮箱验证码.[根据安全设置判断是否执行]
     * 调用/uc/open/phone/rebind.
     * 注意点: 更改安全项, 一定是换什么, 就必须验证什么. 不管是否开启二次验证, 更换手机号必须验证手机.
     */
    @PostMapping("phone/rebind")
    public GenericDto<Object> rebindPhone(@RequestBody RebindPhoneReq rebindPhoneReq) {
        AuthToken newPhoneAuthToken =
                cacheService.getAuthTokenDetailWithToken(rebindPhoneReq.getNewPhoneToken(), ClientAuthTypeEnum.PHONE);

        // 校验新手机令牌
        if (newPhoneAuthToken == null) {
            throw new SecuritySettingException(UcErrorCode.ERR_10032_WRONG_SMS_CODE);
        }

        AuthCode authCode =
                cacheService.getAuthCode(
                        newPhoneAuthToken.getAccountName(),
                        AuthCodeUseTypeEnum.REBIND_PHONE,
                        ClientAuthTypeEnum.PHONE);

        // 拿到安全策略
        Long uid = UserContext.getCurrentUserId();
        List<SecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(uid);

        Map<ClientAuthTypeEnum, SecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(SecurityStrategy::getAuthType, v -> v));
        Optional<SecurityStrategy> emailStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.EMAIL));
        Optional<SecurityStrategy> gaStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.GA));

        // TODO 这里不要select user 表2次了
        UserDto userDto = userService.getUserByUid(uid);

        // 不管是否开启二次验证, 更换手机号必须验证手机
        AuthCode phoneAuthCode =
                cacheService.getAuthCode(
                        userDto.getPhone(),
                        AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_REBIND_PHONE,
                        ClientAuthTypeEnum.PHONE);
        if (phoneAuthCode == null
                || StringUtils.isBlank(phoneAuthCode.getCode())
                || !rebindPhoneReq.getOldSmsCode().equals(phoneAuthCode.getCode())) {
            throw new SecuritySettingException(UcErrorCode.ERR_10032_WRONG_SMS_CODE);
        }

        if (emailStrategy.isPresent() && emailStrategy.get().getNeedAuth()) {
            AuthCode emailAuthCode =
                    cacheService.getAuthCode(
                            userDto.getEmail(),
                            AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_REBIND_PHONE,
                            ClientAuthTypeEnum.EMAIL);
            if (emailAuthCode == null
                    || StringUtils.isBlank(emailAuthCode.getCode())
                    || !rebindPhoneReq.getEmailCode().equals(emailAuthCode.getCode())) {
                throw new SecuritySettingException(UcErrorCode.ERR_10033_WRONG_EMAIL_CODE);
            }
        }
        if (gaStrategy.isPresent() && gaStrategy.get().getNeedAuth()
                && (StringUtils.isBlank(rebindPhoneReq.getGaCode())
                || googleAuthService.verifyUserCode(uid, rebindPhoneReq.getGaCode()))) {
            throw new SecuritySettingException(UcErrorCode.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }

        // 通过了各个验证，修改用户信息
        // TODO 这里不要select user 表2次了，放到service层加transactional
        User user = userMapper.selectByPrimaryKey(userDto.getUid());
        user.setPhone(authCode.getName());
        user.setCountryCode(authCode.getCountryCode());
        userMapper.updateByPrimaryKey(user);
        return GenericDto.success(null);
    }

    /**
     * 调用方法/uc/open/ga_code/verify, 验证GA.
     * 调用方法/uc/open/email_code/send, 参数use_type=VERIFY_SETTING_POLICY_REBIND_GA, 发送邮箱验证码.[根据安全设置判断是否执行]
     * 调用方法/uc/open/sms_code/send, 参数use_type=VERIFY_SETTING_POLICY_REBIND_GA, 发送短信验证码.[根据安全设置判断是否执行]
     * 调用/uc/open/asset_ga/change.
     */
    @PostMapping("ga/change")
    public GenericDto<Object> rebindGA(@RequestBody RebindGaReq rebindGaReq) {
        Long uid = UserContext.getCurrentUserId();

        AuthToken newGaAuthToken =
                cacheService.getAuthTokenDetailWithToken(rebindGaReq.getGaToken(), ClientAuthTypeEnum.GA);

        // 校验GA令牌
        if (newGaAuthToken == null) {
            throw new SecuritySettingException(UcErrorCode.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }

        // 校验ga code
        googleAuthService.verifyUserCode(uid, rebindGaReq.getOldGaCode());

        // 拿到安全策略
        List<SecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(uid);

        Map<ClientAuthTypeEnum, SecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(SecurityStrategy::getAuthType, v -> v));
        Optional<SecurityStrategy> phoneStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.PHONE));
        Optional<SecurityStrategy> emailStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.EMAIL));

        // TODO 这里不要select user 表2次了
        UserDto userDto = userService.getUserByUid(uid);

        // 需要手机验证
        if (phoneStrategy.isPresent() && phoneStrategy.get().getNeedAuth()) {
            AuthCode phoneAuthCode =
                    cacheService.getAuthCode(
                            userDto.getPhone(),
                            AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_REBIND_GA,
                            ClientAuthTypeEnum.PHONE);
            if (phoneAuthCode == null
                    || StringUtils.isBlank(phoneAuthCode.getCode())
                    || !rebindGaReq.getSmsCode().equals(phoneAuthCode.getCode())) {
                throw new SecuritySettingException(UcErrorCode.ERR_10032_WRONG_SMS_CODE);
            }
        }

        if (emailStrategy.isPresent() && emailStrategy.get().getNeedAuth()) {
            AuthCode emailAuthCode =
                    cacheService.getAuthCode(
                            userDto.getEmail(),
                            AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_REBIND_GA,
                            ClientAuthTypeEnum.EMAIL);
            if (emailAuthCode == null
                    || StringUtils.isBlank(emailAuthCode.getCode())
                    || !rebindGaReq.getEmailCode().equals(emailAuthCode.getCode())) {
                throw new SecuritySettingException(UcErrorCode.ERR_10033_WRONG_EMAIL_CODE);
            }
        }

        // 通过了各个验证，修改用户信息
        // TODO 这里不要select user 表2次了，放到service层加transactional
        User user = userMapper.selectByPrimaryKey(userDto.getUid());
        user.setGaSecret(newGaAuthToken.getSecret());
        userMapper.updateByPrimaryKey(user);
        return GenericDto.success(null);
    }

    /**
     * 绑定邮箱
     * 调用方法/uc/open/email_code/send, 参数use_type=BIND_EMAIL, 发送邮箱验证码
     * 调用方法/uc/open/email_code/verify, 参数use_type=BIND_EMAIL, 进行邮箱的验证, 获取到email_token.
     * 调用方法/uc/open/sms_code/send, 参数use_type=VERIFY_SETTING_POLICY_BIND_EMAIL, 发送手机验证码.[根据安全设置判断是否执行]
     * 调用方法/uc/open/security/strategy/verify, 参数use_type=VERIFY_SETTING_POLICY_BIND_EMAIL, 进行安全验证, 获取到auth_token.
     * 调用/uc/open/email/bind, 不使用sms_code, ga_code参数.
     */
    @PostMapping("email/bind")
    public GenericDto<Object> bindEmail(@RequestBody EmailSecurityItemReq securityItemReq) {
        AuthToken emailAuthToken =
                cacheService.getAuthTokenDetailWithToken(securityItemReq.getEmailToken(), ClientAuthTypeEnum.EMAIL);
        if (emailAuthToken == null
                || StringUtils.isBlank(emailAuthToken.getAccountName())) {
            throw new SecurityItemException(UcErrorCode.ERR_10033_WRONG_EMAIL_CODE);
        }
        SecurityAuth securityAuth =
                cacheService.getSecurityAuthWithToken(securityItemReq.getAuthToken());
        if (securityAuth == null) {
            throw new SecuritySettingException(UcErrorCode.ERR_10210_SECURITY_VERIFY_ERROR);
        }
        String accountName = emailAuthToken.getAccountName();
        Long uid = UserContext.getCurrentUserId();
        // 检查account name 是否已经存在
        User checkUser = userMapper.selectByEmail(accountName);
        if (checkUser != null) {
            throw new SecurityItemException(UcErrorCode.ERR_10061_EMAIL_ALREADY_BEEN_USED);
        }
        User userToUpdate = userMapper.selectByPrimaryKey(uid);
        userToUpdate.setEmail(accountName);
        userMapper.updateByPrimaryKey(userToUpdate);

        // 绑定之后set 安全项
        securityStrategyMapper.insert(
                SecurityStrategy.builder()
                        .authType(ClientAuthTypeEnum.EMAIL)
                        .createdAt(System.currentTimeMillis())
                        .needAuth(true)
                        .uid(uid)
                        .updatedAt(System.currentTimeMillis())
                        .build());

        return GenericDto.success(null);
    }

    /**
     * 调用方法/uc/open/ga_code/verify, 验证GA.
     * 调用方法/uc/open/email_code/send, 参数use_type=VERIFY_SETTING_POLICY_BIND_GA, 发送邮箱验证码.[根据安全设置判断是否执行]
     * 调用方法/uc/open/sms_code/send, 参数use_type=VERIFY_SETTING_POLICY_BIND_GA, 发送短信验证码.[根据安全设置判断是否执行]
     * 调用方法/uc/open/security/strategy/verify, 参数use_type=VERIFY_SETTING_POLICY_BIND_GA, 进行安全验证, 获取到auth_token.
     * 调用/uc/open/asset_ga/bind, 不使用参数email_code, sms_code.
     */
    @PostMapping("ga/bind")
    public GenericDto<Object> bindGA(@RequestBody GaSecurityItemReq securityItemReq) {
        AuthToken gaAuthToken =
                cacheService.getAuthTokenDetailWithToken(securityItemReq.getGaToken(), ClientAuthTypeEnum.GA);
        if (gaAuthToken == null
                || StringUtils.isBlank(gaAuthToken.getSecret())) {
            throw new SecurityItemException(UcErrorCode.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }
        SecurityAuth securityAuth =
                cacheService.getSecurityAuthWithToken(securityItemReq.getAuthToken());
        if (securityAuth == null) {
            throw new SecuritySettingException(UcErrorCode.ERR_10210_SECURITY_VERIFY_ERROR);
        }
        Long uid = UserContext.getCurrentUserId();
        User userToUpdate = userMapper.selectByPrimaryKey(uid);
        userToUpdate.setGaSecret(gaAuthToken.getSecret());
        userMapper.updateByPrimaryKey(userToUpdate);

        // 绑定之后set 安全项
        securityStrategyMapper.insert(
                SecurityStrategy.builder()
                        .authType(ClientAuthTypeEnum.GA)
                        .createdAt(System.currentTimeMillis())
                        .needAuth(true)
                        .uid(uid)
                        .updatedAt(System.currentTimeMillis())
                        .build());

        return GenericDto.success(null);
    }


    @GetMapping("ga/generate")
    public GenericDto<GoogleAuthResp> generateGa(HttpServletRequest request) {
        // 用uc token 拿用户的登录名
        String token = WebUtil.getTokenFromRequest(request);
        LoginUser loginUser = cacheService.getUserByToken(token);

        String secret = googleAuthService.genGaSecret();
        cacheService.putGenerateGoogleAuth(token, secret);
        return GenericDto.success(
                GoogleAuthResp.builder()
                        .gaKey(secret)
                        .exchangeName(UcConstants.GA_ISSUER)
                        .loginName(loginUser.getLoginName())
                        .build());
    }
}