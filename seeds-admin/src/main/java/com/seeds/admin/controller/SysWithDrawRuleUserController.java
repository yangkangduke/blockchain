package com.seeds.admin.controller;
import com.seeds.account.dto.req.WithdrawRuleUserSaveOrUpdateReq;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.dto.MgtWithdrawRuleUserDto;
import com.seeds.admin.service.ISysWithdrawRuleUserService;
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
@RequestMapping("/withdraw-rule-user")
@Api(tags = "提币用户规则管理")
public class SysWithDrawRuleUserController {

    @Autowired
    private ISysWithdrawRuleUserService sysWithdrawRuleUserService;

    @GetMapping("/get-list")
    @ApiOperation("获取提币用户规则列表")
    public GenericDto<MgtPageDto<List<MgtWithdrawRuleUserDto>>> getWithdrawRuleUser(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "currency", required = false) String currency,
            @RequestParam(value = "chain", required = false) Integer chain) {
        return sysWithdrawRuleUserService.list(userId, currency,chain);
    }

    @PostMapping("/update")
    @ApiOperation("修改提币用户规则")
    public GenericDto<Boolean> update(@RequestBody WithdrawRuleUserSaveOrUpdateReq req) {
        return sysWithdrawRuleUserService.updateWithdrawRuleUser(req);
    }

    @PostMapping("/add")
    @ApiOperation("新增提币用户规则")
    public GenericDto<Boolean> add(@RequestBody WithdrawRuleUserSaveOrUpdateReq req) {
        return sysWithdrawRuleUserService.addWithdrawRuleUser(req);
    }

    @PostMapping("/switch")
    @ApiOperation("启用、禁用提币用户规则")
    public GenericDto<Boolean> delete(@Valid @RequestBody SwitchReq req) {
        return sysWithdrawRuleUserService.deleteWithdrawRuleUser(req);
    }
}
