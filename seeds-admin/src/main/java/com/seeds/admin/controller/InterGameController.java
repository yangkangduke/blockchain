package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.request.SysGamePageReq;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.admin.dto.response.SysGameBriefResp;
import com.seeds.admin.dto.response.SysGameResp;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.service.SysGameService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 游戏管理内部调用
 * @author hang.yu
 * @date 2022/7/21
 */
@Slf4j
@Api(tags = "游戏管理内部调用")
@RestController
@RequestMapping("/internal-game")
public class InterGameController {

    @Autowired
    private SysGameService sysGameService;

    @PostMapping("uc-page")
    @ApiOperation("分页")
    @Inner
    public GenericDto<IPage<SysGameResp>> ucPage(@Valid @RequestBody SysGamePageReq query) {
        query.setStatus(WhetherEnum.YES.value());
        return GenericDto.success(sysGameService.queryPage(query));
    }

    @GetMapping("uc-detail")
    @ApiOperation("信息")
    @Inner
    public GenericDto<SysGameResp> ucDetail(@RequestParam Long id) {
        return GenericDto.success(sysGameService.detail(id));
    }

    @PostMapping("uc-dropdown-list")
    @ApiOperation("下拉列表")
    @Inner
    public GenericDto<List<SysGameBriefResp>> ucDropdownList() {
        return GenericDto.success(sysGameService.dropdownList());
    }

    @PostMapping("uc-collection")
    @ApiOperation("uc收藏")
    @Inner
    public GenericDto<Object> ucCollection(@RequestParam Long id) {
        sysGameService.ucCollection(id);
        return GenericDto.success(null);
    }

    @GetMapping("secret-key")
    @ApiOperation("查询游戏秘钥")
    @Inner
    public GenericDto<String> querySecretKey(@RequestParam String accessKey) {
        return GenericDto.success(sysGameService.querySecretKey(accessKey));
    }

    @PostMapping("win-rank-info")
    @ApiOperation("获取游戏胜场排行榜")
    @Inner
    public GenericDto<List<GameWinRankResp.GameWinRank>> winRankInfo(@RequestBody GameWinRankReq query) {
        return GenericDto.success(sysGameService.winRankInfo(query));
    }

}
