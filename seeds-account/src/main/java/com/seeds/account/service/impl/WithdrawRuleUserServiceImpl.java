package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.WithdrawRuleUserDto;
import com.seeds.account.dto.req.WithdrawRuleUserSaveOrUpdateReq;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.ConfigException;
import com.seeds.account.mapper.WithdrawRuleUserMapper;
import com.seeds.account.model.SwitchReq;
import com.seeds.account.model.WithdrawRuleUser;
import com.seeds.account.service.IWithdrawRuleUserService;
import com.seeds.account.tool.ListMap;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.seeds.common.enums.ErrorCode.ILLEGAL_WITHDRAW_WHITE_LIST_CONFIG;
import static com.seeds.common.enums.ErrorCode.USER_ID_ON_CHAIN_ALREADY_EXIST;

/**
 * <p>
 * 提币用户规则 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Slf4j
@Service
public class WithdrawRuleUserServiceImpl extends ServiceImpl<WithdrawRuleUserMapper, WithdrawRuleUser> implements IWithdrawRuleUserService {

    final static String ALL = "all";

    @Autowired
    WithdrawRuleUserMapper withdrawRuleUserMapper;

    @Autowired
    UserCenterFeignClient userCenterFeignClient;

    LoadingCache<String, ListMap<WithdrawRuleUserDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(15, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<WithdrawRuleUserDto> list = loadAll();
                Map<String, WithdrawRuleUserDto> map = list.stream()
                        .filter(e -> Objects.equals(e.getStatus(), CommonStatus.ENABLED.getCode()))
                        .collect(Collectors.toMap(e -> toKey(e.getUserId(), e.getCurrency()), e -> e));
                return ListMap.init(list, map);
            });


    private String toKey(long userId, String currency) {
        return userId + ":" + currency;
    }

    @Override
    public WithdrawRuleUserDto get(long userId, String currency) {
        return getAllMap().get(toKey(userId, currency));
    }

    @Override
    public Map<String, WithdrawRuleUserDto> getAllMap() {
        return Objects.requireNonNull(rules.get(ALL)).getMap();
    }

    @Override
    public List<WithdrawRuleUserDto> loadAll() {
        List<WithdrawRuleUserDto> withdraw = withdrawRuleUserMapper.selectList(new QueryWrapper<>())
                .stream()
                .map(e -> ObjectUtils.copy(e, new WithdrawRuleUserDto()))
                .collect(Collectors.toList());
        // rpc 获取uc用户list
        GenericDto<List<UcUserResp>> userListData = userCenterFeignClient.getUserList((withdraw.stream().map(WithdrawRuleUserDto::getUserId).collect(Collectors.toList())));
        List<WithdrawRuleUserDto> resultList = Lists.newArrayList();

        if (null != userListData && userListData.getCode() == 200) {
            for (WithdrawRuleUserDto withdrawDto : withdraw) {

                for (UcUserResp ucUser : userListData.getData()) {
                    if (ucUser.getId().equals(withdrawDto.getUserId())) {
                        withdrawDto.setEmail(ucUser.getEmail());
                        withdrawDto.setPublicAddress(ucUser.getPublicAddress());
                    }
                }
                resultList.add(withdrawDto);
            }
        }
        return resultList;
    }

    @Override
    public Boolean add(WithdrawRuleUserSaveOrUpdateReq req) {
        // 如果用户已经在白名单中，则无法新增；
        this.checkEnableWithdrawList(req.getUserId(),req.getChain());

        WithdrawRuleUser withdrawRuleUser = ObjectUtils.copy(req, WithdrawRuleUser.builder().build());
        withdrawRuleUser.setCreateTime(System.currentTimeMillis());
        withdrawRuleUser.setUpdateTime(System.currentTimeMillis());
        withdrawRuleUser.setVersion(AccountConstants.DEFAULT_VERSION);
        return save(withdrawRuleUser);
    }

    @Override
    public Boolean update(WithdrawRuleUserSaveOrUpdateReq req) {
        log.info("WithdrawRuleUser req = {}",req);
        //userId不能修改为已存在的白名单用户;
        LambdaQueryWrapper<WithdrawRuleUser> queryWrap = new QueryWrapper<WithdrawRuleUser>().lambda()
                .eq(WithdrawRuleUser::getUserId,req.getUserId())
                .eq(WithdrawRuleUser::getChain,req.getChain());
        WithdrawRuleUser one = getOne(queryWrap);
        if (null != one && !one.getId().equals(req.getId())){
            throw  new ConfigException(USER_ID_ON_CHAIN_ALREADY_EXIST);
        }

        WithdrawRuleUser rule = getById(req.getId());
        WithdrawRuleUser withdrawRuleUser = ObjectUtils.copy(req, WithdrawRuleUser.builder().build());
        withdrawRuleUser.setUpdateTime(System.currentTimeMillis());
        withdrawRuleUser.setVersion(rule.getVersion() + 1);
        withdrawRuleUser.setStatus(req.getStatus());
            return updateById(withdrawRuleUser);
        }

    @Override
    public Boolean delete(SwitchReq req) {
        WithdrawRuleUser disableWithdrawRuleUser = WithdrawRuleUser
                .builder()
                .status(CommonStatus.DISABLED.getCode())
                .build();

        WithdrawRuleUser ruleUser = getById(req.getId());
        disableWithdrawRuleUser.setChain(ruleUser.getChain());
        this.update(disableWithdrawRuleUser, new LambdaUpdateWrapper<WithdrawRuleUser>()
                .eq(WithdrawRuleUser::getChain, ruleUser.getChain())
                .eq(WithdrawRuleUser::getId, req.getId()));

        WithdrawRuleUser withdrawRuleUser = WithdrawRuleUser.builder()
                .id(req.getId())
                .status(req.getStatus())
                .build();
        return updateById(withdrawRuleUser);
    }

   private void checkEnableWithdrawList(Long userId,Integer chain) {
        LambdaQueryWrapper<WithdrawRuleUser> queryWrap = new QueryWrapper<WithdrawRuleUser>().lambda()
                .eq(WithdrawRuleUser::getUserId, userId)
                .eq(WithdrawRuleUser::getChain,chain);
        WithdrawRuleUser one = getOne(queryWrap);
        if (one != null) {
            throw new ConfigException(ILLEGAL_WITHDRAW_WHITE_LIST_CONFIG);
        }
    }
}

