package com.seeds.account.monitor;

import com.seeds.account.dto.ChainGasPriceDto;
import com.seeds.account.util.JsonUtils;
import com.seeds.account.util.MetricsGaugeUtils;
import com.seeds.common.enums.Chain;
import com.seeds.common.redis.account.RedisKeys;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author hewei
 */
@Slf4j
@Service
public class GasPriceMonitorService {

    @Autowired
    RedissonClient redissonClient;

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000)
    public void monitor() {
        log.info("GasPriceMonitorService start...");

        Arrays.stream(Chain.values()).forEach(chain -> {
            RBucket<String> rBucket = redissonClient.getBucket(RedisKeys.getChainGasPriceKey(chain.getCode()), StringCodec.INSTANCE);
            if (rBucket.isExists() && StringUtils.isNotEmpty(rBucket.get())) {
                ChainGasPriceDto chainGasPriceDto = JsonUtils.readValue(rBucket.get(), ChainGasPriceDto.class);
                MetricsGaugeUtils.gauge("chainGasPrice", Tags.of("type", "safeGasPrice", "chain", chain.getName()), chainGasPriceDto.getSafeGasPrice());
                MetricsGaugeUtils.gauge("chainGasPrice", Tags.of("type", "proposeGasPrice", "chain", chain.getName()), chainGasPriceDto.getProposeGasPrice());
                MetricsGaugeUtils.gauge("chainGasPrice", Tags.of("type", "fastGasPrice", "chain", chain.getName()), chainGasPriceDto.getFastGasPrice());
            }
        });

        log.info("GasPriceMonitorService end...");
    }
}
