package com.seeds.uc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.dto.response.UcUserAccountInfoResp;
import com.seeds.uc.model.UcUserAccount;
import com.seeds.uc.service.IUcUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 用户账户表 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-07-09
 */
@RestController
@RequestMapping("/account")
@Api(tags = "账户")
public class OpenUserAccountController {

    @Autowired
    private IUcUserAccountService ucUserAccountService;


    @PostMapping("/action")
    @ApiOperation(value = "冲/提币", notes = "1.调用/account/generate-account 生成账户地址" +
            "2.进行OTC操作，调用/account/action接口进行冲/提币")
    public GenericDto<Object> action(@Valid @RequestBody AccountActionReq accountActionReq) {
        ucUserAccountService.action(accountActionReq);
        return GenericDto.success(null);
    }

    @PostMapping("/action/history")
    @ApiOperation(value = "充/提币历史分页", notes = "充/提币历史分页")
    public GenericDto<IPage<AccountActionResp>> actionHistory(@RequestBody AccountActionHistoryReq historyReq) {
        Long currentUserId = UserContext.getCurrentUserId();
        historyReq.setUserId(currentUserId);
        Page page = new Page();
        page.setCurrent(historyReq.getCurrent());
        page.setSize(historyReq.getSize());
        return GenericDto.success(ucUserAccountService.actionHistory(page, historyReq));
    }

    @PostMapping("/generate-account")
    @ApiOperation(value = "生成账户", notes = "生成账户")
    public GenericDto<UcUserAccountInfoResp> generateAccount() {
        Long userId = UserContext.getCurrentUserId();
        UcUserAccount ucUserAccount = ucUserAccountService.getOne(new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, userId));
        if (ucUserAccount == null) {
            // todo 远程调用创建账户
            ucUserAccountService.creatAccountByUserId(userId);
        }

        return GenericDto.success(ucUserAccountService.getInfo());
    }


    @GetMapping("/info")
    @ApiOperation(value = "账户详情", notes = "账户详情")
    public GenericDto<UcUserAccountInfoResp> info() {
        return GenericDto.success(ucUserAccountService.getInfo());
    }
}
