package com.seeds.account.aspect;

import com.seeds.account.anno.ExecutionLock;
import com.seeds.account.ex.MissingLockException;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 使用redis构建一个执行锁
 *
 * @author milo
 */
@Component
@Aspect
@Slf4j
@Order(value = 2)
public class ExecutionLockAspect {

    @Autowired
    RedissonClient client;

    @Around(value = "@annotation(executionLock)")
    public Object doAround(ProceedingJoinPoint joinPoint, ExecutionLock executionLock) throws Throwable {
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] argValues = joinPoint.getArgs();
        String key = executionLock.key();

        for (int i = 0; i < argNames.length; i++) {
            key = StringUtils.replace(key, "{" + argNames[i] + "}", String.valueOf(argValues[i]));
        }

        log.debug("executionLock key={}", key);

        RLock lock = client.getLock(key);
        long lockStartTime = System.currentTimeMillis();
        boolean locked = lock.tryLock(executionLock.waitTime(), executionLock.leaseTime(), executionLock.unit());
        long lockEndTime = System.currentTimeMillis();
        if (!locked) {
            throw new MissingLockException(String.valueOf(executionLock.unit().toMillis(executionLock.waitTime())));
        }
        log.info("executionLock action=lock key={} waitTime={} leaseTime={} lockTime={}",
                key, executionLock.unit().toMillis(executionLock.waitTime()), executionLock.unit().toMillis(executionLock.leaseTime()), (lockEndTime - lockStartTime));
        Metrics.summary("executionLock", Tags.of("action", "lock")).record((lockEndTime - lockStartTime));

        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            try {
                long unlockStartTime = System.currentTimeMillis();
                lock.unlock();
                long unlockEndTime = System.currentTimeMillis();

                log.info("executionLock action=unlock key={} waitTime={} leaseTime={} unlockTime={} total={}",
                        key, executionLock.unit().toMillis(executionLock.waitTime()), executionLock.unit().toMillis(executionLock.leaseTime()),
                        (unlockEndTime - unlockStartTime), (unlockEndTime - lockStartTime));

                Metrics.summary("executionLock", Tags.of("action", "unlock")).record((unlockEndTime - unlockStartTime));
            } catch (Exception e) {
                log.error("failed to unlock key={} total={}", key, (System.currentTimeMillis() - lockStartTime), e);
            }
        }
        return result;
    }
}
