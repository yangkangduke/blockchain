package com.seeds.account.task;

import com.seeds.account.feign.AccountFeignClient;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 执行提币请求，每5秒执行一次
 *
 * @author milo
 *
 */
public class ExecuteWithdrawTask implements SimpleJob {
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public void execute(ShardingContext shardingContext) {
        accountFeignClient.executeWithdraw();
    }
}
