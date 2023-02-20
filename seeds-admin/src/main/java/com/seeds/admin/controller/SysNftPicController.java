package com.seeds.admin.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.SysNftPicPageReq;
import com.seeds.admin.dto.response.SysNftPicResp;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@Api(tags = "NFT上架的管理")
@RestController
@RequestMapping("/nft-pic")
public class SysNftPicController {

    @Autowired
    private SysNftPicService sysNftPicService;

    @PostMapping("page")
    @ApiOperation("获取分页信息")
    public GenericDto<IPage<SysNftPicResp>> queryPage(@Valid @RequestBody SysNftPicPageReq req){
        return GenericDto.success(sysNftPicService.queryPage(req));
    }
}
