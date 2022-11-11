package com.seeds.notification.mq.listener;

import cn.hutool.json.JSONUtil;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.notification.dto.NotificationDto;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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

    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.SEND_NOTIFICATION})
    public void sendNotice(String msg) {
        log.info("收到消息：{}", msg);
        NotificationReq notificationReq = JSONUtil.toBean(msg, NotificationReq.class);
        // sava message to db
        Boolean result = notificationService.saveNotice(notificationReq);
        // send notice
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setSender("seeds-account");
        notificationDto.setNotificationType(notificationReq.getNotificationType());
        notificationDto.setReceivers(notificationReq.getUcUserIds());
        notificationDto.setValues(notificationReq.getValues());
        if (result) {
            notificationService.sendNotice(notificationDto, notificationReq.getUserSource());
        }
    }
}
