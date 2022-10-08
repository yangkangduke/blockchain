package com.seeds.account.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.seeds.account.feign.AccountFeignClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自动创建空闲地址，每分钟运行一次
 *
 * @author milo
 *
 */
public class AutoCreateAddressTask implements SimpleJob {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public void execute(ShardingContext shardingContext) {
        accountFeignClient.scanAndCreateAddresses();
    }
}
