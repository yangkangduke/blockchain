package com.seeds.admin.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author: hewei
 * @date 2023/4/21
 */
@Component
@Slf4j
public class KafkaTestConsumer {


    /**
     * 监听消息
     *
     * @param content
     */
    @KafkaListener(topics = "test_topic", groupId = "tomge-consumer-group")
    public void receiveMesage(String content) {
        log.info("消费消息：" + content);
    }
}
