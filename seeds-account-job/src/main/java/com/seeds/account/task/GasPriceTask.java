package com.seeds.account.task;


import com.seeds.account.feign.AccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 每10秒读取一次
 * 获取链上最新gasPrice,并缓存到Redis
 *
 * @author milo
 */
@Slf4j
@Component
public class GasPriceTask implements SimpleJob {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("GasPriceTask triggered, shardingItem={}", shardingContext.getShardingItem());
        accountFeignClient.getAndMetricCurrentGasPriceOracle();
        log.info("GasPriceTask ended, shardingItem={}", shardingContext.getShardingItem());
    }
}
