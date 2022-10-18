package com.seeds.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.dto.response.NotificationResp;
import com.seeds.notification.feign.RemoteNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hewei
 * @date 2022/9/22
 */
@RestController
@RequestMapping("/notice")
@Api("系统通知")
public class SysNotificationController {

    @Autowired
    private RemoteNoticeService remoteNoticeService;

    @RequestMapping("send-message")
    @ApiOperation(value = "发送消息")
    public GenericDto<Object> sendMessage(@RequestBody NotificationReq req) {
        return remoteNoticeService.sendMessage(req);
    }

    @PostMapping("/getNoticeList")
    public GenericDto<Page<NotificationResp>> getNoticeList(@RequestBody NoticePageReq req) {
        return remoteNoticeService.getNoticeList(req);
    }
}
