package com.seeds.admin.controller;

import com.seeds.account.dto.WithdrawRuleDto;
import com.seeds.account.dto.req.WithdrawRuleReq;
import com.seeds.account.dto.req.WithdrawRuleSaveOrUpdateReq;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.service.ISysDepositWithdrawConfigService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: hewei
 * @date 2022/11/8
 */

@Slf4j
@RestController
@RequestMapping("/withdraw-rule")
@Api(tags = "提币规则配置")
public class SysWithdrawRuleController {


    @Autowired
    private ISysDepositWithdrawConfigService depositWithdrawConfigService;


    @PostMapping("/get-list")
    @ApiOperation("获取提币规则列表")
    public GenericDto<List<WithdrawRuleDto>> getWithdrawRuleList(@RequestBody WithdrawRuleReq req) {
        return depositWithdrawConfigService.getWithdrawRuleList(req);
    }

    @PostMapping("/add")
    @ApiOperation("新增提币规则")
    public GenericDto<Boolean> add(@RequestBody WithdrawRuleSaveOrUpdateReq req) {
        return depositWithdrawConfigService.addWithdrawRule(req);
    }

    @PutMapping("update")
    @ApiOperation("更新提币规则")
    public GenericDto<Boolean> update(@RequestBody WithdrawRuleSaveOrUpdateReq req) {
        return depositWithdrawConfigService.updateWithdrawRule(req);

    }

    @PostMapping("switch")
    @ApiOperation("启用、禁用提币规则")
    public GenericDto<Boolean> delete(@Valid @RequestBody SwitchReq req) {
        return depositWithdrawConfigService.deleteWithdrawRule(req);
    }

}
