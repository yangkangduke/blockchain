package com.seeds.account.service.impl;

import com.seeds.account.service.LockService;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 *
 * @author yk
 *
 */
@Slf4j
@Service
public class LockServiceImpl implements LockService {

    @Autowired
    RedissonClient client;

    @Override
    public <T> T executeInLock(String key, Callable<T> callable) throws Exception {
        RLock lock = client.getLock(key);
        try {
            long startTime = System.currentTimeMillis();
            lock.lock();
            long endTime = System.currentTimeMillis();
            // 记录获取锁所需要的的时间
            log.info("executionLock timeElapsed={} key={} action=lock", (endTime - startTime), key);
            Metrics.summary("executionLock", Tags.of("action", "lock")).record((endTime - startTime));

            return callable.call();
        } finally {
            long startTime = System.currentTimeMillis();
            lock.unlock();
            long endTime = System.currentTimeMillis();
            // 记录释放锁所需要的的时间

            log.info("executionLock timeElapsed={} key={} action=unlock", (endTime - startTime), key);
            Metrics.summary("executionLock", Tags.of("action", "unlock")).record((endTime - startTime));
        }
    }

}
