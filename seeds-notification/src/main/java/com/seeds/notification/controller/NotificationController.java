package com.seeds.notification.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.response.NotificationResp;
import com.seeds.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: hewei
 * @date 2022/9/20
 */
@RequestMapping("/notice")
@RestController
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @PostMapping("/getNoticeList")
    public GenericDto<IPage<NotificationResp>> getNoticeList(@RequestBody NoticePageReq req) {
        return GenericDto.success(notificationService.getNoticeByUserId(req));
    }

    @PutMapping("/updateReadStatus/{id}")
    GenericDto<Boolean> updateReadStatus(@PathVariable("id") Long id) {
            return GenericDto.success(notificationService.updateReadStatus(id));
    }

    @GetMapping("/getUnReadNoticeFlag")
    GenericDto<Boolean> getUnReadNoticeFlag(@RequestParam("userId") Long userId) {
        return GenericDto.success(notificationService.getUnReadNoticeFlag(userId));
    }
}
