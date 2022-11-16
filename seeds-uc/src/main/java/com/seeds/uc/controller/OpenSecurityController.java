package com.seeds.uc.controller;

/**
* @author yk
 * @date 2020/8/26
 */

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.SecurityDetailDto;
import com.seeds.uc.dto.SecurityStrategyDto;
import com.seeds.uc.dto.redis.AuthCodeDTO;
import com.seeds.uc.dto.request.SecuritySettingReq;
import com.seeds.uc.dto.response.TokenResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.SecuritySettingException;
import com.seeds.uc.mapper.UcSecurityStrategyMapper;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "安全策略")
@RequestMapping("/security")
public class OpenSecurityController {
    @Autowired
    IUcUserService userService;
    @Autowired
    UcUserMapper userMapper;
    @Autowired
    CacheService cacheService;
    @Autowired
    IGoogleAuthService googleAuthService;
    @Autowired
    UcSecurityStrategyMapper securityStrategyMapper;

    @GetMapping("/state")
    @ApiOperation(value = "获取安全策略状态", notes = "获取安全策略状态")
    public GenericDto<SecurityDetailDto> getSecurityDetail() {
        Long uid = UserContext.getCurrentUserId();
        List<UcSecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(uid);
        UcUser ucUser = userService.getById(uid);
        Map<ClientAuthTypeEnum, UcSecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(UcSecurityStrategy::getAuthType, v -> v));
        Optional<UcSecurityStrategy> metamaskStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.METAMASK));
        Optional<UcSecurityStrategy> emailStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.EMAIL));
        Optional<UcSecurityStrategy> gaStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.GA));
        return GenericDto.success(
                SecurityDetailDto.builder()
                        .metamask(ucUser.getPublicAddress())
                        .metamaskState(metamaskStrategy.isPresent())
                        .email(ucUser.getEmail())
                        .emailState(emailStrategy.isPresent())
                        .gaState(gaStrategy.isPresent())
                        .build());
    }

    @GetMapping("/strategy/state")
    @ApiOperation(value = "获取安全策略的验证状态", notes = "获取安全策略的验证状态")
    public GenericDto<SecurityStrategyDto> getSecurityStrategy() {
        Long uid = UserContext.getCurrentUserId();
        List<UcSecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(uid);
        Map<ClientAuthTypeEnum, UcSecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(UcSecurityStrategy::getAuthType, v -> v));
        Optional<UcSecurityStrategy> metamaskStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.METAMASK));
        Optional<UcSecurityStrategy> emailStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.EMAIL));
        Optional<UcSecurityStrategy> gaStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.GA));
        return GenericDto.success(
                SecurityStrategyDto.builder()
                        .verifyMetamask(metamaskStrategy.isPresent() && metamaskStrategy.get().getNeedAuth())
                        .verifyEmail(emailStrategy.isPresent() && emailStrategy.get().getNeedAuth())
                        .verifyGA(gaStrategy.isPresent() && gaStrategy.get().getNeedAuth())
                        .build());
    }

    @PostMapping("strategy/verify")
    @ApiOperation(value = "验证安全项", notes = "验证安全项，根据类型判断验证哪些安全项，全部验证成功后，发放authToken ")
    public GenericDto<Object> verifySecurityStrategy(@RequestBody SecuritySettingReq securitySettingReq) {

        // 拿到安全策略
        Long uid = UserContext.getCurrentUserId();
        List<UcSecurityStrategy> securityStrategyList =
                securityStrategyMapper.listByUid(uid);

        Map<ClientAuthTypeEnum, UcSecurityStrategy> strategyEnumMap =
                securityStrategyList
                        .stream()
                        .collect(Collectors.toMap(UcSecurityStrategy::getAuthType, v -> v));
        Optional<UcSecurityStrategy> emailStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.EMAIL));
        Optional<UcSecurityStrategy> gaStrategy = Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.GA));

        UcUser ucUser = userService.getById(uid);

        // 绑定GA只需验证邮箱
        if (AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_BIND_GA.equals(securitySettingReq.getUseType())) {
            // 必须要绑定了email
            if (!emailStrategy.isPresent()) {
                throw new SecuritySettingException(UcErrorCodeEnum.ERR_10210_SECURITY_VERIFY_ERROR);
            }

            if (emailStrategy.isPresent() && emailStrategy.get().getNeedAuth()) {
                AuthCodeDTO emailAuthCode =
                        cacheService.getAuthCode(
                                ucUser.getEmail(),
                                securitySettingReq.getUseType(),
                                ClientAuthTypeEnum.EMAIL);
                if (emailAuthCode == null
                        || StringUtils.isBlank(emailAuthCode.getCode())
                        || !securitySettingReq.getEmailCode().equals(emailAuthCode.getCode())) {
                    throw new SecuritySettingException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
                }
            }

           // 提币需要验证邮箱和GA
        } else if (AuthCodeUseTypeEnum.VERIFY_SETTING_POLICY_WITHDRAW.equals(securitySettingReq.getUseType())) {
            // 必须要绑定了email和GA
            if (!gaStrategy.isPresent() || !emailStrategy.isPresent()) {
                throw new SecuritySettingException(UcErrorCodeEnum.ERR_10210_SECURITY_VERIFY_ERROR);
            }

            if (emailStrategy.isPresent() && emailStrategy.get().getNeedAuth()) {
                AuthCodeDTO emailAuthCode =
                        cacheService.getAuthCode(
                                ucUser.getEmail(),
                                securitySettingReq.getUseType(),
                                ClientAuthTypeEnum.EMAIL);
                if (emailAuthCode == null
                        || StringUtils.isBlank(emailAuthCode.getCode())
                        || !securitySettingReq.getEmailCode().equals(emailAuthCode.getCode())) {
                    throw new SecuritySettingException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
                }
            }

            if (gaStrategy.isPresent() && gaStrategy.get().getNeedAuth()
                && (StringUtils.isBlank(securitySettingReq.getGaCode())
                || !googleAuthService.verifyUserCode(uid, securitySettingReq.getGaCode()))) {

                throw new SecuritySettingException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
            }

        } else {
            throw new SecuritySettingException(UcErrorCodeEnum.ERR_10210_SECURITY_VERIFY_ERROR);
        }

        String authToken = RandomUtil.genRandomToken(uid.toString());
        cacheService.putSecurityAuthTokenWithUidAndUseType(authToken, uid, securitySettingReq.getUseType());
        return GenericDto.success(TokenResp.builder().token(authToken).build());

    }

}
