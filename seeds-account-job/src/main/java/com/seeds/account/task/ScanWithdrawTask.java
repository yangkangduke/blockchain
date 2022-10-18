package com.seeds.account.task;

import com.seeds.account.feign.AccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 查询提币结果,每5秒执行一次
 *
 * @author: hewei
 * @date 2022/10/18
 */
@Slf4j
@Component
public class ScanWithdrawTask implements SimpleJob {
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("scanWithdrawTask triggered, shardingItem={}", shardingContext.getShardingItem());
        accountFeignClient.scanWithdraw();
        log.info("scanWithdrawTask ended, shardingItem={}", shardingContext.getShardingItem());
    }
}
