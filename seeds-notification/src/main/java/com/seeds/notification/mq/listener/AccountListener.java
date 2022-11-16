package com.seeds.notification.mq.listener;

import cn.hutool.json.JSONUtil;
import com.seeds.admin.feign.RemotePermissionService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.notification.dto.NotificationDto;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: hewei
 * @date 2022/10/18
 */
@Slf4j
@Component
public class AccountListener {

    @Resource
    private NotificationService notificationService;
    @Resource
    private RemotePermissionService remotePermissionService;

    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.TOPIC_ACCOUNT_UPDATE})
    public void receiveNotice(String msg) {
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

    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.TOPIC_ACCOUNT_AUDIT})
    public void receiveAuditNotice(String msg) {
        log.info("收到消息：{}", msg);
        NotificationReq notificationReq = JSONUtil.toBean(msg, NotificationReq.class);
        GenericDto<Set<Long>> userResult = remotePermissionService.entitledUsers("auditor");
        if (userResult.isSuccess()) {
            notificationReq.setUcUserIds(new ArrayList<>(userResult.getData()));
        }
        // sava message to db
        Boolean result = notificationService.saveNotice(notificationReq);
        // send notice
        List<Long> userIds = notificationReq.getUcUserIds();
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setSender("seeds-account");
        notificationDto.setNotificationType(notificationReq.getNotificationType());
        notificationDto.setReceivers(userIds);
        notificationDto.setValues(notificationReq.getValues());
        if (result && !CollectionUtils.isEmpty(userIds)) {
            notificationService.sendNotice(notificationDto, notificationReq.getUserSource());
        }
    }
}
