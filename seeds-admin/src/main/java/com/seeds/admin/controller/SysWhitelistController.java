package com.seeds.admin.controller;

import com.seeds.admin.annotation.MgtAuthority;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.dto.MgtWithdrawWhitelistDto;
import com.seeds.admin.service.ISysWhitelistService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
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
    @RequiredPermission("sys:whitelist:list")
    // @MgtAuthority(path = "/user-center/white-user/withdraw/")
        public GenericDto<MgtPageDto<List<MgtWithdrawWhitelistDto>>> getWithdrawWhitelist(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "currency", required = false) String currency) {
        return whitelistService.list(userId, currency);
    }

    @PostMapping("/update")
    @ApiOperation("修改提币白名单")
    @RequiredPermission("sys:whitelist:update")
   // @MgtAuthority(path = "/user-center/white-user/withdraw/:edit")
    public GenericDto<Boolean> update(@RequestBody @Valid MgtWithdrawWhitelistDto dto) {
        return whitelistService.update(dto);
    }

    @PostMapping("/add2")
    @ApiOperation("新增提币白名单")
    public GenericDto<Boolean> add(@RequestBody @Valid MgtWithdrawWhitelistDto dto) {
        return whitelistService.add(dto);
    }
}
