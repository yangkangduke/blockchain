package com.seeds.account.task;

import com.seeds.account.feign.AccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 执行提币请求，每5秒执行一次
 *
 * @author: hewei
 * @date 2022/10/18
 */

@Slf4j
@Component
public class ExecuteWithdrawTask implements SimpleJob {
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("executeWithdrawTask triggered, shardingItem={}", shardingContext.getShardingItem());
        accountFeignClient.executeWithdraw();
        log.info("executeWithdrawTask triggered, shardingItem={}", shardingContext.getShardingItem());
    }
}
