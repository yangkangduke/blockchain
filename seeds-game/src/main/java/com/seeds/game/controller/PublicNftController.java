package com.seeds.game.controller;

import com.seeds.admin.dto.request.SysNftHonorModifyReq;
import com.seeds.admin.dto.request.SysNftModifyReq;
import com.seeds.admin.dto.request.SysNftSettlementReq;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 提供外部调用的NFT相关接口
 * @author hang.yu
 * @date 2022/10/08
 */
@Slf4j
@Api(tags = "NFT外部调用开放")
@RestController
@RequestMapping("/public/nft")
public class PublicNftController {

    @Autowired
    private RemoteNftService remoteNftService;

    @PostMapping("modify")
    @ApiOperation("NFT修改")
    public GenericDto<Object> modify(@Valid @RequestBody SysNftModifyReq req) {
        return remoteNftService.modify(req);
    }

    @PostMapping("honor-modify")
    @ApiOperation("NFT战绩更新")
    public GenericDto<Object> honorModify(@Valid @RequestBody List<SysNftHonorModifyReq> req) {
        return remoteNftService.honorModify(req);
    }

    @PostMapping("settlement")
    @ApiOperation("NFT结算")
    public GenericDto<Object> lock(@Valid @RequestBody SysNftSettlementReq req) {
        return remoteNftService.settlement(req);
    }

}
