package com.seeds.admin.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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
    public void send(String topic, String msg) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            // 在数据库事务提交之后在发送消息
            @Override
            public void afterCommit() {
                log.info("发送消息： topic - {},msg - {}", topic, msg);
                kafkaTemplate.send(topic, msg);
            }
        });

    }

    /**
     * 回调发送信息 是否成功
     */
    @Transactional
    public void CallBackSend(String topic, String msg) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            // 在数据库事务提交之后在发送消息
            @Override
            public void afterCommit() {
                log.info("发送消息： topic - {},msg - {}", topic, msg);
                kafkaTemplate.send(topic, msg).addCallback(success -> {
                    // 消息发送到的topic
                    String topics = success.getRecordMetadata().topic();
                    // 消息发送到的分区
                    int partition = success.getRecordMetadata().partition();
                    // 消息在分区内的offset
                    long offset = success.getRecordMetadata().offset();
                    System.out.println("发送消息成功:" + topics + "-" + partition + "-" + offset);
                }, failure -> {
                    System.out.println("发送消息失败:" + failure.getMessage());
                });
            }
        });
    }
}
