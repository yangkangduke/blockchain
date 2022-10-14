package com.seeds.account.task;

import com.seeds.account.feign.AccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 同步块,每5秒执行一次
 *
 * @author milo
 *
 */
@Slf4j
@Component
public class ScanChainBlockTask implements SimpleJob {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("scanChainBlockTask triggered, shardingItem={}", shardingContext.getShardingItem());
        accountFeignClient.scanBlock();
        log.info("scanChainBlockTask ended, shardingItem={}", shardingContext.getShardingItem());
    }
}
