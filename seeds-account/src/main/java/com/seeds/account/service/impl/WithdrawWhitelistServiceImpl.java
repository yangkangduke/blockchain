package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.WithdrawWhitelistDto;
import com.seeds.account.dto.req.WithdrawWhitelistSaveOrUpdateReq;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.ConfigException;
import com.seeds.account.mapper.WithdrawWhitelistMapper;
import com.seeds.account.model.SwitchReq;
import com.seeds.account.model.WithdrawWhitelist;
import com.seeds.account.service.IWithdrawWhitelistService;
import com.seeds.account.tool.ListMap;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.seeds.common.enums.ErrorCode.*;

/**
 * <p>
 * 提币白名单 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Slf4j
@Service
public class WithdrawWhitelistServiceImpl extends ServiceImpl<WithdrawWhitelistMapper, WithdrawWhitelist> implements IWithdrawWhitelistService {

    final static String ALL = "all";

    @Autowired
    WithdrawWhitelistMapper withdrawWhitelistMapper;

    @Autowired
    UserCenterFeignClient userCenterFeignClient;

    LoadingCache<String, ListMap<WithdrawWhitelistDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(15, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<WithdrawWhitelistDto> list = loadAll();
                Map<String, WithdrawWhitelistDto> map = list.stream()
                        .filter(e -> Objects.equals(e.getStatus(), CommonStatus.ENABLED.getCode()))
                        .collect(Collectors.toMap(e -> toKey(e.getUserId(), e.getCurrency()), e -> e));
                return ListMap.init(list, map);
            });


    private String toKey(long userId, String currency) {
        return userId + ":" + currency;
    }

    @Override
    public WithdrawWhitelistDto get(long userId, String currency) {
        return getAllMap().get(toKey(userId, currency));
    }

    @Override
    public Map<String, WithdrawWhitelistDto> getAllMap() {
        return Objects.requireNonNull(rules.get(ALL)).getMap();
    }

    @Override
    public List<WithdrawWhitelistDto> loadAll() {
        List<WithdrawWhitelistDto> withdraw = withdrawWhitelistMapper.selectList(new QueryWrapper<>())
                .stream()
                .map(e -> ObjectUtils.copy(e, new WithdrawWhitelistDto()))
                .collect(Collectors.toList());
        GenericDto<Map<Long, String>> emailMap = userCenterFeignClient.getEmailByIds(withdraw.stream().map(WithdrawWhitelistDto::getUserId).collect(Collectors.toList()));

        List<WithdrawWhitelistDto> resultList = Lists.newArrayList();

        if (null != emailMap && emailMap.getCode() == 200) {
            for (WithdrawWhitelistDto withdrawDto : withdraw) {
                WithdrawWhitelistDto dto = new WithdrawWhitelistDto();
                ObjectUtils.copy(withdrawDto, dto);
                resultList.add(dto);
            }
        }
        return resultList;
    }

    @Override
    public Boolean add(WithdrawWhitelistSaveOrUpdateReq req) {
        // 如果用户已经在白名单中，则无法新增；
        this.checkEnableWithdrawList(req.getUserId(),req.getChain());

        WithdrawWhitelist withdrawWhitelist = ObjectUtils.copy(req, WithdrawWhitelist.builder().build());
        withdrawWhitelist.setCreateTime(System.currentTimeMillis());
        withdrawWhitelist.setUpdateTime(System.currentTimeMillis());
        withdrawWhitelist.setVersion(AccountConstants.DEFAULT_VERSION);
        return save(withdrawWhitelist);
    }

    @Override
    public Boolean update(WithdrawWhitelistSaveOrUpdateReq req) {
        //userId不能修改为已存在的白名单用户;
        LambdaQueryWrapper<WithdrawWhitelist> queryWrap = new QueryWrapper<WithdrawWhitelist>().lambda()
                .eq(WithdrawWhitelist::getUserId,req.getUserId())
                .eq(WithdrawWhitelist::getChain,req.getChain());
        WithdrawWhitelist one = getOne(queryWrap);
        if (null != one){
            throw  new ConfigException(USER_ID_ON_CHAIN_ALREADY_EXIST);
        }

        WithdrawWhitelist rule = getById(req.getId());
        WithdrawWhitelist withdrawWhitelist = ObjectUtils.copy(req, WithdrawWhitelist.builder().build());
        withdrawWhitelist.setUpdateTime(System.currentTimeMillis());
        withdrawWhitelist.setVersion(rule.getVersion() + 1);
        withdrawWhitelist.setStatus(req.getStatus());
            return updateById(withdrawWhitelist);
        }

    @Override
    public Boolean delete(SwitchReq req) {
        WithdrawWhitelist disableWhitelist = WithdrawWhitelist
                .builder()
                .status(CommonStatus.DISABLED.getCode())
                .build();

        WithdrawWhitelist whitelist = getById(req.getId());
        disableWhitelist.setChain(whitelist.getChain());
        this.update(disableWhitelist, new LambdaUpdateWrapper<WithdrawWhitelist>()
                .eq(WithdrawWhitelist::getChain, whitelist.getChain())
                .eq(WithdrawWhitelist::getId, req.getId()));

        WithdrawWhitelist withdrawWhitelist = WithdrawWhitelist.builder()
                .id(req.getId())
                .status(req.getStatus())
                .build();
        return updateById(withdrawWhitelist);
    }

   private void checkEnableWithdrawList(Long userId,Integer chain) {
        LambdaQueryWrapper<WithdrawWhitelist> queryWrap = new QueryWrapper<WithdrawWhitelist>().lambda()
                .eq(WithdrawWhitelist::getUserId, userId)
                .eq(WithdrawWhitelist::getChain,chain);
        WithdrawWhitelist one = getOne(queryWrap);
        if (one != null) {
            throw new ConfigException(ILLEGAL_WITHDRAW_WHITE_LIST_CONFIG);
        }
    }
}

