package com.seeds.notification.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.TargetSource;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.response.NotificationResp;
import com.seeds.notification.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: hewei
 * @date 2022/9/20
 */
@RequestMapping("/notice")
@RestController
@Api(tags = "通知")
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @PostMapping("/getNoticeList")
    @ApiOperation("获取用户的所有通知")
    public GenericDto<IPage<NotificationResp>> getNoticeList(@RequestBody NoticePageReq req) {
        return GenericDto.success(notificationService.getNoticeByUserId(req));
    }

    @PutMapping("/updateReadStatus/{id}")
    @ApiOperation("更新通知为已读状态")
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
    GenericDto<Boolean> getUnReadNoticeFlag(@RequestParam("ucUserId") Long ucUserId,
                                            @RequestParam(value = "userSource", required = false) String userSource) {
        if (StringUtils.isEmpty(userSource)) {
            userSource = TargetSource.UC.name();
        }
        return GenericDto.success(notificationService.getUnReadNoticeFlag(ucUserId, userSource));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除通知")
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
