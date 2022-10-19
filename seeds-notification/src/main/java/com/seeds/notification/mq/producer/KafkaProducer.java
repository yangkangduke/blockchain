package com.seeds.notification.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;

/**
 * 生产者
 *
 * @author hewei
 */
@Component
@Slf4j
public class KafkaProducer {


    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 发送消息
     */
    @Transactional
    @Async
    public void sendAsync(String topic, String msg) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            // 在数据库事务提交之后在发送消息
            @Override
            public void afterCommit() {
                log.info("发送消息： topic - {},msg - {}", topic, msg);
                kafkaTemplate.send(topic, msg);
            }
        });

    }

}
