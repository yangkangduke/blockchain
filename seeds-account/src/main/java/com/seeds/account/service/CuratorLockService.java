package com.seeds.account.service;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

public interface CuratorLockService {

    InterProcessMutex getLock(String path);

    boolean getAcquire(InterProcessMutex lock, long time, TimeUnit unit);

    void releaseLock(InterProcessMutex lock, boolean acquire);
}
