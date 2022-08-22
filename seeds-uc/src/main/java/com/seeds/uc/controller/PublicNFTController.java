package com.seeds.uc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.UcNftPageReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * NFT 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-08-19
 */
@RestController
@RequestMapping("/public/nft")
@Api(tags = "公共NFT")
public class PublicNFTController {


    @Autowired
    private RemoteNftService remoteNftService;

    @PostMapping("/uc-page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public GenericDto<Page<SysNftResp>> page(UcNftPageReq query) {
        return remoteNftService.ucPage(query);
    }

    @GetMapping("/uc-detail/{id}")
    @ApiOperation("信息")
    public GenericDto<SysNftDetailResp> detail(@PathVariable("id") Long id) {
        return remoteNftService.ucDetail(id);
    }

}
