package com.seeds.account.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.seeds.account.feign.AccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 同步块,每5秒执行一次
 *
 * @author yk
 *
 */
@Slf4j
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
