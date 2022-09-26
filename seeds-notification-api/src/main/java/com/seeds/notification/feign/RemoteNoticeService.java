package com.seeds.notification.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.notification.feign.interceptor.FeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.request.NoticeSaveReq;
import com.seeds.notification.dto.response.NotificationResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author hewei
 * @date 2022/9/20
 */
@FeignClient(name = "RemoteNoticeService", url = "${Seeds-notification}", path = "/internal-notice", configuration = {FeignInnerRequestInterceptor.class})
public interface RemoteNoticeService {

    @PostMapping("/getNoticeList")
    GenericDto<Page<NotificationResp>> getNoticeList(@RequestBody NoticePageReq req);

    @PostMapping("/sendMessage")
    GenericDto<Object> sendMessage(@RequestBody NoticeSaveReq req);
}
