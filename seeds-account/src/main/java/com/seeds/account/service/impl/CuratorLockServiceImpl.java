package com.seeds.account.service.impl;

import com.seeds.account.service.CuratorLockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CuratorLockServiceImpl implements CuratorLockService {

    @Autowired 
    private CuratorFramework curatorFramework;

    /**
     * InterProcessMutex：分布式可重入排它锁
     * InterProcessSemaphoreMutex：分布式排它锁
     * InterProcessReadWriteLock：分布式读写锁
     * InterProcessMultiLock：将多个锁作为单个实体管理的容器
     *
     * @param path
     * @return
     */
    @Override
    public InterProcessMutex getLock(String path) {
        return new InterProcessMutex(curatorFramework, path);
    }

    @Override
    public boolean getAcquire(InterProcessMutex lock, long time, TimeUnit unit) {
        boolean acquire = false;
        try {
            acquire = lock.acquire(time, unit);
        } catch (Exception e) {
            log.error("could not get zk lock, due to ", e);
        }
        return acquire;
    }

    @Override
    public void releaseLock(InterProcessMutex lock, boolean acquire) {
        try {
            if (acquire) {
                log.debug("release lock={}", lock.getParticipantNodes());
                lock.release();
            }
        } catch (Exception e) {
            log.error("could not release zk lock, due to", e);
        }
    }
}
