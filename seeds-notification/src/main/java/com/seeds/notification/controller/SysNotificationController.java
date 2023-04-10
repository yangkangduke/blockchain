//package com.seeds.notification.controller;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.seeds.common.dto.GenericDto;
//import com.seeds.common.enums.TargetSource;
//import com.seeds.notification.dto.request.SysNoticePageReq;
//import com.seeds.notification.dto.response.NotificationResp;
//import com.seeds.notification.dto.response.SysNotificationResp;
//import com.seeds.notification.service.ISysNotificationService;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
///**
// * <p>
// * 系统通知 前端控制器
// * </p>
// *
// * @author hewei
// * @since 2023-03-22
// */
//@RestController
//@RequestMapping("/notification/sysNotification")
//public class SysNotificationController {
//
//    @Autowired
//    private ISysNotificationService sysNotificationService;
//
//
//    @PostMapping("/page")
//    @ApiOperation("获取系统通知分页列表")
//    public GenericDto<IPage<SysNotificationResp>> getNoticePage(@RequestBody SysNoticePageReq req) {
//        return GenericDto.success(sysNotificationService.getNoticePage(req));
//    }
//
//
//    @GetMapping("/detail/{id}")
//    @ApiOperation("获取通知详情")
//    GenericDto<SysNotificationResp> detail(@PathVariable("id") Long id) {
//        return GenericDto.success(sysNotificationService.detail(id));
//    }
//
//    @DeleteMapping("/delete/{id}")
//    @ApiOperation("删除通知")
//    GenericDto<Boolean> delete(@PathVariable("id") Long id) {
//        return GenericDto.success(sysNotificationService.delete(id));
//    }
//
//    @DeleteMapping("/delete-all")
//    @ApiOperation("删除全部通知")
//    GenericDto<Boolean> deleteAll(@RequestParam(value = "userId") Long userId,
//                                  @RequestParam(value = "userSource", required = false) String userSource) {
//        if (StringUtils.isEmpty(userSource)) {
//            userSource = TargetSource.UC.name();
//        }
//        return GenericDto.success(notificationService.deleteAll(userId, userSource));
//    }
//
//
//}
