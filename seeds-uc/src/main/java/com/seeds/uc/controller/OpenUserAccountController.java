package com.seeds.uc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.LoginUser;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.service.IUcUserAccountService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
@Api(tags = "用户账号相关接口")
public class OpenUserAccountController {
    @Autowired
    private IUcUserAccountService ucUserAccountService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private IUcUserService ucUserService;

    /**
     * 冲/提币
     */
    @PostMapping("/action")
    @ApiOperation(value = "冲/提币", notes = "冲/提币")
    public GenericDto<Object> action(@Valid @RequestBody AccountActionReq accountActionReq, HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUser loginUser = cacheService.getUserByToken(loginToken);
        if (loginUser == null || ucUserService.getById(loginUser.getUserId()).getMetamaskFlag() != 1) {
            throw new InvalidArgumentsException("Account does not exist");
        }
        return GenericDto.success(ucUserAccountService.action(accountActionReq, loginUser));
    }

    /**
     * 充/提币历史分页
     */
    @PostMapping("/action/history")
    @ApiOperation(value = "充/提币历史分页", notes = "充/提币历史分页")
    public GenericDto<Page<AccountActionResp>> actionHistory(@RequestBody AccountActionHistoryReq historyReq) {
        return GenericDto.success(ucUserAccountService.actionHistory(historyReq));
    }

}
