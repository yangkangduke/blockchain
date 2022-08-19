package com.seeds.uc.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.UcNftPageReq;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 开放接口
 *
 * @author hang.yu
 * @date  2022-08-19
 */
@RestController
@Api(tags = "开放接口")
@RequestMapping("/open-api")
public class OpenApiController {

    @Autowired
    private RemoteNftService remoteNftService;

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/uc-page")
    public GenericDto<IPage<SysNftResp>> getSysFilePage(UcNftPageReq query) {
        return remoteNftService.ucPage(query);
    }

}
