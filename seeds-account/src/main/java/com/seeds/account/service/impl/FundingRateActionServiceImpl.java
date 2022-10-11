package com.seeds.account.service.impl;

import com.seeds.account.service.IFundingRateActionService;
import com.seeds.common.redis.account.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author guocheng
 * @date 2020/12/30
 */

@Slf4j
@Service
public class FundingRateActionServiceImpl implements IFundingRateActionService {


    @Autowired
    private RedissonClient client;

    @Override
    public boolean isAnySettlingAsset() {
        RMap<String, String> settleLockMap = client.getMap(RedisKeys.getFundingRateGlobalSettleLockKey(), StringCodec.INSTANCE);
        return settleLockMap.size() > 0;
    }
}
