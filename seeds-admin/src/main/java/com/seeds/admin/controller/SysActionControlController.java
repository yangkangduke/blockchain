package com.seeds.admin.controller;

import com.seeds.account.dto.ActionControlDto;
import com.seeds.admin.dto.MgtActionControlDto;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.service.ISysRiskService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/action-control")
@Api(tags = "操作控制类")
public class SysActionControlController {

    @Autowired
    ISysRiskService ISysRiskService;

    @GetMapping("/action-control")
    @ApiOperation("获取所有系统操作控制")
    public GenericDto<MgtPageDto<List<ActionControlDto>>> getActionControlList() {
        return ISysRiskService.getAllActionControl();
    }

    @PostMapping("/update-action-control")
    @ApiOperation("更新系统操作控制")
    public GenericDto<Boolean> updateActionControl(@RequestBody MgtActionControlDto dto) {
        return ISysRiskService.updateActionControl(dto);
    }

    @PostMapping("/add-action-control")
    @ApiOperation("添加新的系统操作控制")
    public GenericDto<Boolean> addActionControl(@RequestBody MgtActionControlDto dto) {
        return ISysRiskService.addActionControl(dto);
    }
}
