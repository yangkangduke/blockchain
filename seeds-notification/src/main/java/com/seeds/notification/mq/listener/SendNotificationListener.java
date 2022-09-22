package com.seeds.notification.mq.listener;

import cn.hutool.json.JSONUtil;
import com.seeds.common.mq.constant.KafkaTopic;
import com.seeds.notification.dto.NoticeDTO;
import com.seeds.notification.dto.request.NoticeSaveReq;
import com.seeds.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: hewei
 * @date 2022/9/21
 */
@Component
@Slf4j
public class SendNotificationListener {

    @Resource
    private NotificationService notificationService;

    @KafkaListener(groupId = "notice-consumer-group", topics = {KafkaTopic.SEND_NOTICE})
    public void sendNotice(String msg, Acknowledgment ack) {
        log.info("收到消息：{}", msg);
        NoticeDTO msgDTO = JSONUtil.toBean(msg, NoticeDTO.class);
        // sava message to db
        NoticeSaveReq saveReq = new NoticeSaveReq();
        saveReq.setContent(msgDTO.getContent());
        saveReq.setUcUserId(msgDTO.getUcUserId());
        Boolean result = notificationService.saveNotice(saveReq);
        // send notice
        if (result) {
            notificationService.sendNotice(msgDTO);
        }
        //手动提交offset
        ack.acknowledge();
    }
}
