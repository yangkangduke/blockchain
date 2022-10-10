package com.seeds.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.OpenNftHonorModifyReq;
import com.seeds.game.dto.request.OpenNftModifyReq;
import com.seeds.game.dto.request.OpenNftPageReq;
import com.seeds.game.dto.request.OpenNftSettlementReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public GenericDto<Object> modify(@Valid @RequestBody OpenNftModifyReq req) {
        return remoteNftService.modify(req);
    }

    @PostMapping("honor-modify")
    @ApiOperation("NFT战绩更新")
    public GenericDto<Object> honorModify(@Valid @RequestBody OpenNftHonorModifyReq req) {
        return remoteNftService.honorModify(req.getHonorList());
    }

    @PostMapping("settlement")
    @ApiOperation("NFT结算")
    public GenericDto<Object> lock(@Valid @RequestBody OpenNftSettlementReq req) {
        return remoteNftService.settlement(req);
    }

    @PostMapping("trade-page")
    @ApiOperation("NFT交易列表")
    public GenericDto<Page<SysNftResp>> tradePage(@Valid @RequestBody OpenNftPageReq req) {
        return remoteNftService.tradePage(req);
    }

    @GetMapping("/detail")
    @ApiOperation("NFT交易详情")
    public GenericDto<SysNftDetailResp> detail(@RequestParam Long id,
                                               @RequestParam String accessKey,
                                               @RequestParam String signature,
                                               @RequestParam Long timestamp) {
        return remoteNftService.tradeDetail(id);
    }
}
