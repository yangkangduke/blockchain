package com.seeds.notification.controller.rpc;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.dto.response.NotificationResp;
import com.seeds.notification.mq.producer.KafkaProducer;
import com.seeds.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: hewei
 * @date 2022/9/22
 */
@RequestMapping("/internal-notice")
@RestController
@Slf4j
public class RpcNotificationController {

    @Resource
    private KafkaProducer kafkaProducer;

    @Resource
    private NotificationService notificationService;

    @PostMapping("/sendMessage")
    public GenericDto<Object> sendMessage(@RequestBody NotificationReq req) {

        kafkaProducer.sendAsync(KafkaTopic.SEND_NOTIFICATION, JSONUtil.toJsonStr(req));
        return GenericDto.success(null);
    }

    @PostMapping("/getNoticeList")
    public GenericDto<IPage<NotificationResp>> getNoticeList(@RequestBody NoticePageReq req) {
        return GenericDto.success(notificationService.getNoticeByUserId(req));
    }
}
