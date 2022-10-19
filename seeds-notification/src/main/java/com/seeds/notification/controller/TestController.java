package com.seeds.notification.controller;

import cn.hutool.json.JSONUtil;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.mq.producer.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: hewei
 */
@RestController
@RequestMapping("notice-test")
@Slf4j
public class TestController {

    @Resource
    private KafkaProducer kafkaProducer;

    @PostMapping("/sendMessage")
    public GenericDto<Object> sendMessage(@RequestBody NotificationReq req) {

        Long currentUserId = UserContext.getCurrentUserId();
        log.info("sendMessage test , currentUserId:{}", currentUserId);
        kafkaProducer.sendAsync(KafkaTopic.SEND_NOTIFICATION, JSONUtil.toJsonStr(req));
        return GenericDto.success(null);
    }
}
