package com.seeds.admin.mq.consumer;

import com.seeds.admin.service.SysRandomCodeService;
import com.seeds.common.constant.mq.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消费者
 *
 * @author hewei
 */
@Component
@Slf4j
public class RandomCodeConsumer {

    @Resource
    private SysRandomCodeService sysRandomCodeService;

    /**
     * 消费随机码生成消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "random-code-consumer-group", topics = {KafkaTopic.RANDOM_CODE_GENERATE})
    public void generateCode(String msg) {
        log.info("收到消息：{}", msg);
        sysRandomCodeService.generateCode(msg);
    }
    /**
     * 消费随机码导出消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "random-code-consumer-group", topics = {KafkaTopic.RANDOM_CODE_EXPORT})
    public void exportCode(String msg) {
        log.info("收到消息：{}", msg);
        sysRandomCodeService.exportCode(msg);
    }

}
