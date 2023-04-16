package com.seeds.admin.controller;

import com.seeds.admin.dto.SkinNftPushAutoIdReq;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: hewei
 * @date 2023/4/16
 */

@Slf4j
@Api(tags = "皮肤nft内部调用")
@RestController
@RequestMapping("/internal-skin-nft")
public class InterSkinNftController {

    @Autowired
    private SysNftPicService nftPicService;

    @PostMapping("push-autoId")
    @ApiOperation("推送autoId")
    @Inner
    public GenericDto<Object> pushAutoId(@Valid @RequestBody SkinNftPushAutoIdReq req) {
        nftPicService.pushAutoId(req);
        return GenericDto.success(null);
    }

}
