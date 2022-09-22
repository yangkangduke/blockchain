package com.seeds.notification.controller;

import cn.hutool.json.JSONUtil;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.mq.constant.KafkaTopic;
import com.seeds.common.web.context.UserContext;
import com.seeds.notification.dto.NoticeDTO;
import com.seeds.notification.dto.request.NoticeSaveReq;
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
    public GenericDto<Object> sendMessage(@RequestBody NoticeSaveReq req) {

        Long currentUserId = UserContext.getCurrentUserId();
        log.info("sendMessage test , currentUserId:{}", currentUserId);

        NoticeDTO msgDTO = new NoticeDTO();
        msgDTO.setUcUserId(req.getUcUserId());
        msgDTO.setContent(req.getContent());
        kafkaProducer.send(KafkaTopic.SEND_NOTICE, JSONUtil.toJsonStr(msgDTO));
        return GenericDto.success(null);
    }
}
