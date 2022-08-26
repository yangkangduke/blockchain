package com.seeds.admin.controller;

import com.seeds.admin.dto.response.SysGameTypeResp;
import com.seeds.admin.service.SysGameTypeService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 游戏类别管理内部调用
 * @author hang.yu
 * @date 2022/8/26
 */
@Slf4j
@Api(tags = "游戏类别管理内部调用")
@RestController
@RequestMapping("/internal-game-type")
public class InterGameTypeController {

    @Autowired
    private SysGameTypeService sysGameTypeService;

    @GetMapping("uc-dropdown")
    @ApiOperation("uc下拉列表")
    @Inner
    public GenericDto<List<SysGameTypeResp>> ucDropdown() {
        return GenericDto.success(sysGameTypeService.queryRespList(null));
    }

}
