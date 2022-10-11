package com.seeds.account.service;

import java.util.concurrent.Callable;

/**
 *
 * @author yk
 *
 */
public interface ILockService {
    /**
     * 在获取lock的情况下执行
     * @param key
     * @param callable
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T executeInLock(String key, Callable<T> callable) throws Exception;
}
