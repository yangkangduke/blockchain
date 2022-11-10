package com.seeds.admin.controller;

import com.seeds.account.dto.WithdrawLimitRuleDto;
import com.seeds.account.dto.req.ListReq;
import com.seeds.account.dto.req.WithdrawLimitSaveOrUpdateReq;
import com.seeds.admin.service.ISysDepositWithdrawConfigService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hewei
 * @date 2022/11/8
 */

@Slf4j
@RestController
@RequestMapping("/withdraw-limit-rule")
@Api(tags = "提币限额规则配置")
public class SysWithdrawLimitRuleController {


    @Autowired
    private ISysDepositWithdrawConfigService depositWithdrawConfigService;


    @PostMapping("get-list")
    @ApiOperation("获取提币限额规则列表")
    public GenericDto<List<WithdrawLimitRuleDto>> getWithdrawLimitRuleList() {
        return depositWithdrawConfigService.getWithdrawLimitRuleList();
    }

    @PostMapping("/add")
    @ApiOperation("新增提币规则")
    public GenericDto<Boolean> add(@RequestBody WithdrawLimitSaveOrUpdateReq req) {
        return depositWithdrawConfigService.addWithdrawLimitRule(req);
    }

    @PutMapping("update")
    @ApiOperation("更新提币规则")
    public GenericDto<Boolean> update(@RequestBody WithdrawLimitSaveOrUpdateReq req) {
        return depositWithdrawConfigService.updateWithdrawLimitRule(req);

    }

//    @PostMapping("/sys/delete-/withdraw-limit-rule")
//    @ApiOperation("删除提币规则")
//    public GenericDto<Boolean> delete(@Valid @RequestBody ListReq req) {
//        return depositWithdrawConfigService.deleteWithdrawLimitRule(req);
//    }

}
