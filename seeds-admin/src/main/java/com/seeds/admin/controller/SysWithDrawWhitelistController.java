package com.seeds.admin.controller;
import com.seeds.account.dto.req.WithdrawWhitelistSaveOrUpdateReq;
import com.seeds.account.model.SwitchReq;
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
@RequestMapping("/WithDrawWhitelist")
@Api(tags = "提币白名单管理")
public class SysWithDrawWhitelistController {

    @Autowired
    private ISysWhitelistService whitelistService;

    @GetMapping("/get-list")
    @ApiOperation("获取提币白名单列表")
    public GenericDto<MgtPageDto<List<MgtWithdrawWhitelistDto>>> getWithdrawWhitelist(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "currency", required = false) String currency) {
        return whitelistService.list(userId, currency);
    }

    @PostMapping("/update")
    @ApiOperation("修改提币白名单")
    public GenericDto<Boolean> update(@RequestBody WithdrawWhitelistSaveOrUpdateReq req) {
        return whitelistService.updateWithdrawWhiteList(req);
    }

    @PostMapping("/add")
    @ApiOperation("新增提币白名单")
    public GenericDto<Boolean> add(@RequestBody WithdrawWhitelistSaveOrUpdateReq req) {
        return whitelistService.addWithdrawWhiteList(req);
    }

    @PostMapping("/switch")
    @ApiOperation("启用、禁用提币白名单")
    public GenericDto<Boolean> delete(@Valid @RequestBody SwitchReq req) {
        return whitelistService.deleteWithdrawWhiteList(req);
    }
}
