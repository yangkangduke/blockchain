package com.seeds.admin.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysGameVideosReq;
import com.seeds.admin.dto.response.SysGameVideosResp;
import com.seeds.admin.feign.interceptor.AdminFeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @author hewei
 * @date 2023/6/25
 */
@FeignClient(name = "remoteGameVideosService", url = "${service.url.admin}", configuration = {AdminFeignInnerRequestInterceptor.class})
public interface RemoteGameVideosService {

    @PostMapping("/internal-videos/page")
    @ApiOperation("分页")
    GenericDto<Page<SysGameVideosResp>> queryPage(@RequestBody SysGameVideosReq req);

    @PostMapping("/internal-videos/top-videos")
    @ApiOperation("置顶视频")
    GenericDto<List<SysGameVideosResp>> getTopVideos();

}
