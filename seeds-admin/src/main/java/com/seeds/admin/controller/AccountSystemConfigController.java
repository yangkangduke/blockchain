package com.seeds.admin.controller;

import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.account.dto.AccountSystemConfigDto;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 账户系统配置管理
 * @author hang.yu
 * @date 2022/11/09
 */
@Slf4j
@Api(tags = "账户系统配置管理")
@RestController
@RequestMapping("/account-system-config")
public class AccountSystemConfigController {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiredPermission("sys:accountSystemConfig:list")
    public GenericDto<List<AccountSystemConfigDto>> list() {
        return accountFeignClient.accountSystemConfigList();
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:accountSystemConfig:modify")
    public GenericDto<Object> modify(@Validated @RequestBody AccountSystemConfigDto req) {
        return GenericDto.success(accountFeignClient.accountSystemConfigModify(req));
    }

}
