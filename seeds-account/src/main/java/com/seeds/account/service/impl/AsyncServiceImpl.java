package com.seeds.account.service.impl;

import com.seeds.account.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 由于service内部调用中注解是无效的，因为增加一层调用类实现事务操作
 *
 * @author shilz
 */
@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    @Override
    @Async
    public void execute(Runnable runnable) {
        runnable.run();
    }
}
