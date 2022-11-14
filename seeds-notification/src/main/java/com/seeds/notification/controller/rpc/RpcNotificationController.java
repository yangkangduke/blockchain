package com.seeds.notification.controller.rpc;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.TargetSource;
import com.seeds.common.web.inner.Inner;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.dto.response.NotificationResp;
import com.seeds.notification.mq.producer.KafkaProducer;
import com.seeds.notification.service.NotificationService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @Inner
    public GenericDto<Object> sendMessage(@RequestBody NotificationReq req) {

        kafkaProducer.sendAsync(KafkaTopic.SEND_NOTIFICATION, JSONUtil.toJsonStr(req));
        return GenericDto.success(null);
    }

    @PostMapping("/getNoticeList")
    @Inner
    public GenericDto<IPage<NotificationResp>> getNoticeList(@RequestBody NoticePageReq req) {
        return GenericDto.success(notificationService.getNoticeByUserId(req));
    }

    @PutMapping("/updateReadStatus/{id}")
    @ApiOperation("更新通知为已读状态")
    @Inner
    GenericDto<Boolean> updateReadStatus(@PathVariable("id") Long id) {
        return GenericDto.success(notificationService.updateReadStatus(id));
    }

    @PutMapping("/read-all")
    @ApiOperation("全部已读")
    GenericDto<Boolean> readAll(@RequestParam("userId") Long userId,
                                @RequestParam(value = "userSource", required = false) String userSource) {
        if (StringUtils.isEmpty(userSource)) {
            userSource = TargetSource.UC.name();
        }
        return GenericDto.success(notificationService.readAll(userId, userSource));
    }


    @GetMapping("/getUnReadNoticeFlag")
    @ApiOperation("用户是否有未读消息")
    @Inner
    GenericDto<Boolean> getUnReadNoticeFlag(@RequestParam("ucUserId") Long ucUserId,
                                            @RequestParam(value = "userSource", required = false) String userSource) {
        if (StringUtils.isEmpty(userSource)) {
            userSource = TargetSource.UC.name();
        }
        return GenericDto.success(notificationService.getUnReadNoticeFlag(ucUserId, userSource));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除通知")
    @Inner
    GenericDto<Boolean> delete(@PathVariable("id") Long id) {
        return GenericDto.success(notificationService.delete(id));
    }


    @DeleteMapping("/delete-all")
    @ApiOperation("删除全部通知")
    GenericDto<Boolean> deleteAll(@RequestParam(value = "userId") Long userId,
                                  @RequestParam(value = "userSource", required = false) String userSource) {
        if (StringUtils.isEmpty(userSource)) {
            userSource = TargetSource.UC.name();
        }
        return GenericDto.success(notificationService.deleteAll(userId, userSource));
    }

}
