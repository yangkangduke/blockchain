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
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("获取用户的所有通知")
    public GenericDto<Page<NotificationResp>> getNoticeList(@RequestBody NoticePageReq req) {
        return remoteNoticeService.getNoticeList(req);
    }

    @PutMapping("/updateReadStatus/{id}")
    @ApiOperation("更新通知为已读状态")
    public GenericDto<Boolean> updateReadStatus(@PathVariable("id") Long id) {
        return remoteNoticeService.updateReadStatus(id);
    }

    @GetMapping("/getUnReadNoticeFlag")
    @ApiOperation("用户是否有未读消息")
    public GenericDto<Boolean> getUnReadNoticeFlag(@RequestParam("ucUserId") Long ucUserId,
                                                   @RequestParam("userSource") String userSource) {
        return remoteNoticeService.getUnReadNoticeFlag(ucUserId, userSource);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除通知")
    GenericDto<Boolean> delete(@PathVariable("id") Long id) {
        return remoteNoticeService.delete(id);
    }

    @PutMapping("/read-all")
    @ApiOperation("全部已读")
    GenericDto<Boolean> readAll(@RequestParam("userId") Long userId,
                                @RequestParam(value = "userSource") String userSource) {
        return remoteNoticeService.readAll(userId, userSource);
    }

    @DeleteMapping("/delete-all")
    @ApiOperation("删除全部通知")
    GenericDto<Boolean> deleteAll(@RequestParam(value = "userId") Long userId,
                                  @RequestParam(value = "userSource") String userSource) {
        return remoteNoticeService.deleteAll(userId, userSource);
    }

}
