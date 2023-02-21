package com.seeds.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.PageReq;
import com.seeds.admin.dto.request.SysGameSrcAddReq;
import com.seeds.admin.dto.request.SysNftPicUpHisReq;
import com.seeds.admin.dto.response.SysNftPicUpHisResp;
import com.seeds.admin.service.SysNftPicUpHisService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * NFT上传文件记录
 * @author yang.deng
 * @date 2023/2/20
 */
@Slf4j
@Api(tags = "NFT上传文件记录")
@RestController
@RequestMapping("/nft-pic-up-history")
public class SysNftPicUpHisController {

    @Autowired
    private SysNftPicUpHisService sysNftPicUpHisService;

    @PostMapping("page")
    @ApiOperation("获取分页信息")
    public GenericDto<IPage<SysNftPicUpHisResp>>queryPage(@Valid @RequestBody PageReq req){
        return GenericDto.success(sysNftPicUpHisService.queryPage(req));
    }


    @PostMapping("upload")
    @ApiOperation(value = "上传")
    public GenericDto<Boolean> upload(@RequestPart(value = "files") MultipartFile[] files, @Valid SysNftPicUpHisReq req) {
        sysNftPicUpHisService.upload(files, req);
        return GenericDto.success(null);
    }
}
