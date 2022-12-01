package com.seeds.account.task;

import com.seeds.account.feign.RemoteAccountTradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * <p>
 * NFT任务 前端控制器
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-06
 */
@Slf4j
@Component
public class NftOfferExpiredTask implements SimpleJob {

    @Autowired
    private RemoteAccountTradeService remoteAccountTradeService;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("nftOfferExpired triggered, shardingItem={}", shardingContext.getShardingItem());
        remoteAccountTradeService.nftOfferExpired();
        log.info("nftOfferExpired ended, shardingItem={}", shardingContext.getShardingItem());
    }
}
