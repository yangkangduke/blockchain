package com.seeds.account.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.anno.SingletonLock;
import com.seeds.account.mapper.UserAccountMapper;
import com.seeds.account.service.IWalletAccountService;
import com.seeds.account.util.JsonUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.redis.account.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author milo
 */
@Slf4j
@Service
public class WalletAccountServiceImpl implements IWalletAccountService {

    @Autowired
    RedissonClient client;
    @Autowired
    UserAccountMapper userAccountMapper;

    @Override
    public boolean freeze(Long userId, String currency, BigDecimal amount) {
        log.info("freeze userId={} currency={} amount={}", userId, currency, amount);

        Assert.isTrue(userId != null && userId >= 0, "user Id must > 0");
        Assert.isTrue(currency != null && currency.length() > 0, "invalid currency");
        Assert.isTrue(amount != null && amount.signum() > 0, "invalid amount");
        return userAccountMapper.freeze(userId, currency, amount) == 1;
    }
}
