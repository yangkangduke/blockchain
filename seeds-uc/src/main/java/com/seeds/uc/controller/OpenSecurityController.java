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
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.mapper.UcSecurityStrategyMapper;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.impl.CacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation(value = "获取安全策略验证状态", notes = "获取安全策略验证状态")
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


}
