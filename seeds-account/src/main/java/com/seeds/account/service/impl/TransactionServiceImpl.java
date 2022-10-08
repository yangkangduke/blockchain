package com.seeds.account.service.impl;

import com.seeds.account.service.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.Callable;

/**
 * 由于service内部调用中注解是无效的，因为增加一层调用类实现事务操作
 *
 * @author shilz
 *
 */
@Slf4j
@Service
public class TransactionServiceImpl implements ITransactionService {

/*    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(Runnable runnable) throws Exception {
        runnable.run();
    }*/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> T execute(Callable<T> callable) throws Exception {
        return callable.call();
    }

    @Override
    public void afterCommit(Runnable runnable) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalArgumentException("isSynchronizationActive = false");
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                try {
                    // 由于原有的事务已经commit，因此如果此处有调用的话，需要自己重启transaction或者用Async执行
                    runnable.run();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        });
    }
}
