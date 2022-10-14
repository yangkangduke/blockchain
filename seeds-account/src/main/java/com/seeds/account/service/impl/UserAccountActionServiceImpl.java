package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.AccountConstants;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import com.seeds.account.mapper.UserAccountActionHisMapper;
import com.seeds.account.mapper.UserAccountMapper;
import com.seeds.account.model.UserAccountActionHis;
import com.seeds.account.service.IUserAccountActionService;
import com.seeds.account.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * user account action service
 *
 * query, transfer, exchange, withdraw, stake, stake & mint, mint, unlock, unlock & burn, & burn
 *
 */
@Slf4j
@Service
public class UserAccountActionServiceImpl implements IUserAccountActionService {

    @Autowired
    UserAccountMapper userAccountMapper;

    @Autowired
    UserAccountActionHisMapper userAccountActionHisMapper;

    @Override
    public void createHistory(long userId, String currency, AccountAction action, BigDecimal amount, CommonActionStatus status) {
        createHistory(userId, currency, action, "", amount, status);

    }

    @Override
    @Async("executorPool")
    public void createHistory(long userId, String currency, AccountAction action, BigDecimal amount) {
        createHistory(userId, currency, action, amount, CommonActionStatus.SUCCESS);
    }

    @Override
    public int updateStatusByActionUserIdSource(AccountAction action, long userId, String source, CommonActionStatus status) {
        log.info("updateStatusByActionUserIdSource action={} userId={} source={} status={}", action, userId, source, status);

        Assert.isTrue(source != null && source.length() > 0, "invalid source");
        return userAccountActionHisMapper.updateStatusByActionUserIdSource(action, userId, source, status);
    }

    @Override
    @Async("executorPool")
    public void createHistory(long userId, String currency, AccountAction action, String source, BigDecimal amount, CommonActionStatus status) {
        UserAccountActionHis o = UserAccountActionHis.builder()
                .userId(userId)
                .currency(currency)
                .amount(amount)
                .action(action)
                .source(source)
                .status(status)
                .version(Integer.valueOf((int) AccountConstants.DEFAULT_VERSION))
                .createTime(System.currentTimeMillis())
                .updateTime(System.currentTimeMillis())
                .build();
        log.info("createHistory {}", o);
        userAccountActionHisMapper.insert(o);
    }
}
