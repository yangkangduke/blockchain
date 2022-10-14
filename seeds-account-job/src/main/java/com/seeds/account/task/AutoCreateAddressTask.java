package com.seeds.account.task;

import com.seeds.account.feign.AccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自动创建空闲地址，每分钟运行一次
 *
 * @author milo
 *
 */
@Slf4j
@Component
public class AutoCreateAddressTask implements SimpleJob {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("autoCreateAddressTask triggered, shardingItem={}", shardingContext.getShardingItem());
        accountFeignClient.scanAndCreateAddresses();
        log.info("autoCreateAddressTask ended, shardingItem={}", shardingContext.getShardingItem());

    }
}
