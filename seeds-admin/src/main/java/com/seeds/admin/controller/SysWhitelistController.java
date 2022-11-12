package com.seeds.admin.controller;

import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.dto.MgtWithdrawWhitelistDto;
import com.seeds.admin.service.ISysWhitelistService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/whitelist")
@Api(tags = "提币白名单管理")
public class SysWhitelistController {

    @Autowired
    private ISysWhitelistService whitelistService;

    @GetMapping("/list")
    @ApiOperation("获取提币白名单列表")
    public GenericDto<MgtPageDto<List<MgtWithdrawWhitelistDto>>> getWithdrawWhitelist(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "currency", required = false) String currency) {
        return whitelistService.list(userId, currency);
    }

    @PostMapping("/update")
    @ApiOperation("修改提币白名单")
    public GenericDto<Boolean> update(@RequestBody MgtWithdrawWhitelistDto dto) {
        return whitelistService.update(dto);
    }

    @PostMapping("/add")
    @ApiOperation("新增提币白名单")
    public GenericDto<Boolean> add(@RequestBody MgtWithdrawWhitelistDto dto) {
        return whitelistService.add(dto);
    }
}
