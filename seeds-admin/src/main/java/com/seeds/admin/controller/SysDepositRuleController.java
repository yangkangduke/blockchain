package com.seeds.admin.controller;

import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.req.DepositRuleReq;
import com.seeds.account.dto.req.DepositRuleSaveOrUpdateReq;
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
@RequestMapping("/deposit-rule")
@Api(tags = "充币规则配置")
public class SysDepositRuleController {


    @Autowired
    private ISysDepositWithdrawConfigService depositWithdrawConfigService;


    @PostMapping("get-list")
    @ApiOperation("获取充币规则列表")
    public GenericDto<List<DepositRuleDto>> getDepositRuleList(@RequestBody DepositRuleReq req) {
        return depositWithdrawConfigService.getDepositRuleList(req);
    }

    @PostMapping("/add")
    @ApiOperation("新增充币规则")
    public GenericDto<Boolean> add(@RequestBody DepositRuleSaveOrUpdateReq req) {
        return depositWithdrawConfigService.addDepositRule(req);
    }

    @PutMapping("update")
    @ApiOperation("更新充币规则")
    public GenericDto<Boolean> update(@RequestBody DepositRuleSaveOrUpdateReq req) {
        return depositWithdrawConfigService.updateDepositRule(req);

    }

    @PostMapping("/switch")
    @ApiOperation("启用、禁用充币规则")
    public GenericDto<Boolean> delete(@Valid @RequestBody SwitchReq req) {
        return depositWithdrawConfigService.deleteDepositRule(req);
    }

}
