package com.seeds.account.task;

import com.seeds.account.feign.AccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 查询待归集地址余额
 *
 * @author hewei
 */
@Slf4j
@Component
public class ScanPendingCollectBalancesTask implements SimpleJob {
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("scanPendingCollectBalancesTask triggered, shardingItem={}", shardingContext.getShardingItem());
        accountFeignClient.scanPendingCollectBalances();
        log.info("scanPendingCollectBalancesTask ended, shardingItem={}", shardingContext.getShardingItem());
    }
}
