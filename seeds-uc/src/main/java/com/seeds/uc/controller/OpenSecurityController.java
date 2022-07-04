package com.seeds.uc.controller;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.SecurityDetailDto;
import com.seeds.uc.dto.SecurityStrategyDto;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.AuthCode;
import com.seeds.uc.dto.redis.SecurityAuth;
import com.seeds.uc.dto.request.SecuritySettingReq;
import com.seeds.uc.dto.response.TokenResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.exceptions.SecuritySettingException;
import com.seeds.uc.mapper.SecurityStrategyMapper;
import com.seeds.uc.mapper.UserMapper;
import com.seeds.uc.model.SecurityStrategy;
import com.seeds.uc.model.User;
import com.seeds.uc.service.CacheService;
import com.seeds.uc.service.GoogleAuthService;
import com.seeds.uc.service.UserService;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户安全策略详情，设置
 */
@Slf4j
@RestController
@RequestMapping("/uc/security")
public class OpenSecurityController {
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CacheService cacheService;

    @Autowired
    GoogleAuthService googleAuthService;

    @Autowired
    SecurityStrategyMapper securityStrategyMapper;

    @GetMapping("get")
    public GenericDto<SecurityDetailDto> getSecurityDetail() {
        Long uid = UserContext.getCurrentUserId();
        List<SecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(uid);
        UserDto userDto = userService.getUserByUid(uid);
        Map<ClientAuthTypeEnum, SecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(SecurityStrategy::getAuthType, v -> v));
        Optional<SecurityStrategy> phoneStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.PHONE));
        Optional<SecurityStrategy> emailStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.EMAIL));
        Optional<SecurityStrategy> gaStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.GA));
        return GenericDto.success(
                SecurityDetailDto.builder()
                        .countryCode(userDto.getCountryCode())
                        .phone(userDto.getPhone())
                        .phoneState(phoneStrategy.isPresent())
                        .email(userDto.getEmail())
                        .emailState(emailStrategy.isPresent())
                        .gaState(gaStrategy.isPresent())
                        .build());
    }

    @GetMapping("strategy/get")
    public GenericDto<SecurityStrategyDto> getSecurityStrategy() {
        Long uid = UserContext.getCurrentUserId();
        List<SecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(uid);
        Map<ClientAuthTypeEnum, SecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(SecurityStrategy::getAuthType, v -> v));
        Optional<SecurityStrategy> phoneStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.PHONE));
        Optional<SecurityStrategy> emailStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.EMAIL));
        Optional<SecurityStrategy> gaStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.GA));
        return GenericDto.success(
                SecurityStrategyDto.builder()
                        .verifyPhone(phoneStrategy.isPresent() && phoneStrategy.get().getNeedAuth())
                        .verifyEmail(emailStrategy.isPresent() && emailStrategy.get().getNeedAuth())
                        .verifyGA(gaStrategy.isPresent() && gaStrategy.get().getNeedAuth())
                        .build());
    }

    @PostMapping("strategy/verify")
    public GenericDto<Object> verifySecurityStrategy(@RequestBody SecuritySettingReq securitySettingReq) {

        // 拿到安全策略
        Long uid = UserContext.getCurrentUserId();
        List<SecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(uid);

        Map<ClientAuthTypeEnum, SecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(SecurityStrategy::getAuthType, v -> v));
        Optional<SecurityStrategy> phoneStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.PHONE));
        Optional<SecurityStrategy> emailStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.EMAIL));
        Optional<SecurityStrategy> gaStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.GA));

        UserDto userDto = userService.getUserByUid(uid);

        // 需要手机验证
        if (phoneStrategy.isPresent() && phoneStrategy.get().getNeedAuth()) {
            AuthCode phoneAuthCode =
                    cacheService.getAuthCode(
                            userDto.getPhone(),
                            securitySettingReq.getUseType(),
                            ClientAuthTypeEnum.PHONE);
            if (phoneAuthCode == null
                    || StringUtils.isBlank(phoneAuthCode.getCode())
                    || !securitySettingReq.getSmsCode().equals(phoneAuthCode.getCode())) {
                throw new SecuritySettingException(UcErrorCode.ERR_10032_WRONG_SMS_CODE);
            }
        }
        if (emailStrategy.isPresent() && emailStrategy.get().getNeedAuth()) {
            AuthCode emailAuthCode =
                    cacheService.getAuthCode(
                            userDto.getEmail(),
                            securitySettingReq.getUseType(),
                            ClientAuthTypeEnum.EMAIL);
            if (emailAuthCode == null
                    || StringUtils.isBlank(emailAuthCode.getCode())
                    || !securitySettingReq.getEmailCode().equals(emailAuthCode.getCode())) {
                throw new SecuritySettingException(UcErrorCode.ERR_10033_WRONG_EMAIL_CODE);
            }
        }
        if (gaStrategy.isPresent() && gaStrategy.get().getNeedAuth()
                && (StringUtils.isBlank(securitySettingReq.getGaCode())
                || googleAuthService.verifyUserCode(uid, securitySettingReq.getGaCode()))) {
            throw new SecuritySettingException(UcErrorCode.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }

        String authToken = RandomUtil.genRandomToken(uid.toString());
        cacheService.putSecurityAuthTokenWithUidAndUseType(authToken, uid, securitySettingReq.getUseType());
        return GenericDto.success(TokenResp.builder().token(authToken).build());
    }

    // 开启某一项，只需要验证该项即可，因为并不是修改
    @PostMapping("strategy/enable")
    public GenericDto<Object> enableSecurityStrategy(@RequestBody SecuritySettingReq securitySettingReq) {
        User user = userMapper.selectByPrimaryKey(UserContext.getCurrentUserId());
        // 验证该项
        if (ClientAuthTypeEnum.GA.equals(securitySettingReq.getItem())
                && (StringUtils.isBlank(securitySettingReq.getGaCode())
                || !googleAuthService.verify(securitySettingReq.getGaCode(), user.getGaSecret()))) {
            throw new SecuritySettingException(UcErrorCode.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
        }
        if (ClientAuthTypeEnum.PHONE.equals(securitySettingReq.getItem())) {
            AuthCode phoneAuthCode =
                    cacheService.getAuthCode(user.getPhone(),
                            AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_ENABLE_PHONE_POLICY,
                            ClientAuthTypeEnum.PHONE);
            if (phoneAuthCode == null
                    || StringUtils.isBlank(phoneAuthCode.getCode())
                    || !phoneAuthCode.getCode().equals(securitySettingReq.getSmsCode())) {
                throw new SecuritySettingException(UcErrorCode.ERR_10032_WRONG_SMS_CODE);
            }
        }
        if (ClientAuthTypeEnum.EMAIL.equals(securitySettingReq.getItem())) {
            AuthCode emailAuthCode =
                    cacheService.getAuthCode(user.getEmail(),
                            AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_ENABLE_EMAIL_POLICY,
                            ClientAuthTypeEnum.EMAIL);
            if (emailAuthCode == null
                    || StringUtils.isBlank(emailAuthCode.getCode())
                    || !emailAuthCode.getCode().equals(securitySettingReq.getEmailCode())) {
                throw new SecuritySettingException(UcErrorCode.ERR_10033_WRONG_EMAIL_CODE);
            }
        }

        // 拿到安全策略
        List<SecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(user.getId());
        Map<ClientAuthTypeEnum, SecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(SecurityStrategy::getAuthType, v -> v));
        Optional<SecurityStrategy> strategyToSet = Optional.ofNullable(strategyEnumMap.get(securitySettingReq.getItem()));
        // TODO 未绑定等错误细分
        if (!strategyToSet.isPresent()) {
            throw new SecuritySettingException(UcErrorCode.ERR_10210_SECURITY_VERIFY_ERROR);
        } else if (strategyToSet.get().getNeedAuth()) {
            throw new SecuritySettingException(UcErrorCode.ERR_11120_SECURITY_ITEM_ENABLED_ALREADY);
        }
        strategyToSet.get().setNeedAuth(true);
        securityStrategyMapper.updateByPrimaryKey(strategyToSet.get());
        return GenericDto.success(null);
    }

    // 关闭某一项，需要验证所有的安全项，所以需要auth token
    @PostMapping("strategy/disable")
    public GenericDto<Object> disableSecurityStrategy(@RequestBody SecuritySettingReq securitySettingReq) {
        SecurityAuth securityAuth =
                cacheService.getSecurityAuthWithToken(securitySettingReq.getAuthToken());
        if (securityAuth == null) {
            throw new SecuritySettingException(UcErrorCode.ERR_10210_SECURITY_VERIFY_ERROR);
        }
        // 拿到安全策略
        Long uid = UserContext.getCurrentUserId();
        List<SecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(uid);
        Map<ClientAuthTypeEnum, SecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(SecurityStrategy::getAuthType, v -> v));
        Optional<SecurityStrategy> strategyToSet = Optional.ofNullable(strategyEnumMap.get(securitySettingReq.getItem()));
        // TODO 未绑定
        if (!strategyToSet.isPresent()) {
            throw new SecuritySettingException(UcErrorCode.ERR_10210_SECURITY_VERIFY_ERROR);
        } else if (!strategyToSet.get().getNeedAuth()) {
            throw new SecuritySettingException(UcErrorCode.ERR_11121_SECURITY_ITEM_DISABLED_ALREADY);
        }
        strategyToSet.get().setNeedAuth(false);
        securityStrategyMapper.updateByPrimaryKey(strategyToSet.get());
        return GenericDto.success(null);
    }
}
