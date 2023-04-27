package com.seeds.uc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysGamePageReq;
import com.seeds.admin.dto.response.SysGameBriefResp;
import com.seeds.admin.dto.response.SysGameResp;
import com.seeds.admin.dto.response.SysGameTypeResp;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.admin.feign.RemoteKolService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 游戏 前端控制器
 * </p>
 *
 * @author hang.yu
 * @date 2022-08-22
 */
@RestController
@RequestMapping("/public/game")
@Api(tags = "公共游戏")
public class PublicGameController {

    @Autowired
    private RemoteGameService remoteGameService;

    @Autowired
    private RemoteKolService remoteKolService;

    @PostMapping("/page")
    @ApiOperation(value = "分页", notes = "分页")
    public GenericDto<Page<SysGameResp>> queryPage(@Valid @RequestBody SysGamePageReq query) {
        return remoteGameService.ucPage(query);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation(value = "信息", notes = "信息")
    public GenericDto<SysGameResp> detail(@PathVariable("id") Long id) {
        return remoteGameService.ucDetail(id);
    }

    @PostMapping("/dropdown-list")
    @ApiOperation(value = "下拉列表", notes = "下拉列表")
    public GenericDto<List<SysGameBriefResp>> dropdownList() {
        return remoteGameService.ucDropdownList();
    }

    @PostMapping("/uc-collection/{id}")
    @ApiOperation("uc收藏")
    public GenericDto<Object> ucCollection(@PathVariable("id") Long id) {
        return remoteGameService.ucCollection(id);
    }

    @GetMapping("/type/uc-dropdown")
    @ApiOperation("游戏类别列表")
    public GenericDto<List<SysGameTypeResp>> typeDropdown() {
        return remoteGameService.ucTypeDropdown();
    }

    @GetMapping("/invite-code/{inviteNo}")
    @ApiOperation("获取邀请码")
    public GenericDto<String> inviteCode(@PathVariable String inviteNo) {
        return remoteKolService.inviteCode(inviteNo);
    }

}
