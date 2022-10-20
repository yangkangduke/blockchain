package com.seeds.account.aspect;

import com.seeds.account.anno.SingletonLock;
import com.seeds.account.service.CuratorLockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author yk
 */
@Component
@Aspect
@Slf4j
@Order(value = 1)
public class SingletonLockAspect {

    @Autowired
    private CuratorLockService curatorLockService;

    @Around(value = "@annotation(singletonLock)")
    public Object doAround(ProceedingJoinPoint joinPoint, SingletonLock singletonLock) throws Throwable {
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] argValues = joinPoint.getArgs();
        String key = singletonLock.key();

        for (int i = 0; i < argNames.length; i++) {
            key = StringUtils.replace(key, "{" + argNames[i] + "}", String.valueOf(argValues[i]));
        }

        log.debug("singletonLock key={}", key);
        InterProcessMutex lock = curatorLockService.getLock(key);
        boolean acquire = curatorLockService.getAcquire(lock, /*singletonLock.waitTime()*/ -1, singletonLock.unit());
        if (acquire) {
            log.debug("singletonLock key={} acquire={}", key, acquire);
        } else {
            log.warn("singletonLock key={} acquire={}", key, acquire);
        }

        try {
            if (acquire) {
                return joinPoint.proceed();
            } else {
                return null;
            }
        } finally {
            curatorLockService.releaseLock(lock, acquire);
        }
    }

}
