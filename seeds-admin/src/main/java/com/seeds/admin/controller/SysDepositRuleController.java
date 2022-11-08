package com.seeds.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.req.DepositRulePageReq;
import com.seeds.account.dto.req.DepositRuleSaveOrUpdateReq;
import com.seeds.account.dto.req.ListReq;
import com.seeds.admin.service.ISysDepositWithdrawConfigService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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


    @PostMapping("/sys/get-deposit-rule-list")
    @ApiOperation("获取充币规则列表")
    public GenericDto<Page<DepositRuleDto>> getDepositRuleList(@RequestBody DepositRulePageReq req) {
        return depositWithdrawConfigService.getList(req);
    }

    @PostMapping("/sys/add-deposit-rule")
    @ApiOperation("新增充币规则")
    public GenericDto<Boolean> add(@RequestBody DepositRuleSaveOrUpdateReq req) {
        return depositWithdrawConfigService.add(req);
    }

    @PutMapping("/sys/update-deposit-rule")
    @ApiOperation("更新充币规则")
    public GenericDto<Boolean> update(@RequestBody DepositRuleSaveOrUpdateReq req) {
        return depositWithdrawConfigService.update(req);

    }

    @PostMapping("/sys/delete-deposit-rule")
    @ApiOperation("删除充币规则")
    public GenericDto<Boolean> delete(@Valid @RequestBody ListReq req) {
        return depositWithdrawConfigService.delete(req);
    }

}
