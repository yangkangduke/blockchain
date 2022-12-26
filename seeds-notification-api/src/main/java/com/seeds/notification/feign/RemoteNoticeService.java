package com.seeds.notification.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.notification.feign.interceptor.FeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.dto.response.NotificationResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * @author hewei
 * @date 2022/9/20
 */
@FeignClient(name = "RemoteNoticeService", url = "${service.url.notification}", path = "/internal-notice", configuration = {FeignInnerRequestInterceptor.class})
public interface RemoteNoticeService {

    @PostMapping("/getNoticeList")
    GenericDto<Page<NotificationResp>> getNoticeList(@RequestBody NoticePageReq req);

    @PostMapping("/sendMessage")
    GenericDto<Object> sendMessage(@RequestBody NotificationReq req);

    @PutMapping("/updateReadStatus/{id}")
    GenericDto<Boolean> updateReadStatus(@PathVariable("id") Long id);

    @GetMapping("/getUnReadNoticeFlag")
    GenericDto<Boolean> getUnReadNoticeFlag(@RequestParam("ucUserId") Long ucUserId,
                                            @RequestParam(value = "userSource", required = false) String userSource);

    @DeleteMapping("/delete/{id}")
    GenericDto<Boolean> delete(@PathVariable("id") Long id);

    @PutMapping("/read-all")
    GenericDto<Boolean> readAll(@RequestParam("userId") Long userId,
                                @RequestParam(value = "userSource", required = false) String userSource);

    @DeleteMapping("/delete-all")
    GenericDto<Boolean> deleteAll(@RequestParam(value = "userId") Long userId,
                                  @RequestParam(value = "userSource", required = false) String userSource);


}
