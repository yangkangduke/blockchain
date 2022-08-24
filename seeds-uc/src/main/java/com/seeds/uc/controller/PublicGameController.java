package com.seeds.uc.controller;


import com.seeds.admin.dto.response.SysGameBriefResp;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/dropdown-list")
    @ApiOperation(value = "下拉列表", notes = "下拉列表")
    public GenericDto<List<SysGameBriefResp>> dropdownList() {
        return remoteGameService.ucDropdownList();
    }

}
