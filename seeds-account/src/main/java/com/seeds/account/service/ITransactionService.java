package com.seeds.account.service;

import java.util.concurrent.Callable;

/**
 *
 *
 */
public interface ITransactionService {

//    /**
//     * 在事务中执行，无返回值
//     * @param runnable
//     * @throws Exception
//     */
//    void execute(Runnable runnable) throws Exception;

    /**
     * 在事务中执行，有回值
     * @param callable
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T execute(Callable<T> callable) throws Exception;

    /**
     *
     * 当事务执行成功后执行的操作
     * @param runnable
     */
    void afterCommit(Runnable runnable);
}
