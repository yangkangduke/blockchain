package com.seeds.uc.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.request.UcUserAddressReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.mapper.UcUserAddressMapper;
import com.seeds.uc.model.UcUserAddress;
import com.seeds.uc.service.IUcUserAccountService;
import com.seeds.uc.service.IUcUserAddressService;
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
@Api(tags = "账号")
public class OpenUserAccountController {

    @Autowired
    private IUcUserAccountService ucUserAccountService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private IUcUserAddressService ucUserAddressService;

    /**
     * 冲/提币
     */
    @PostMapping("/action")
    @ApiOperation(value = "冲/提币", notes = "冲/提币")
    public GenericDto<Object> action(@Valid @RequestBody AccountActionReq accountActionReq, HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        if (loginUser == null ) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_13000_ACCOUNT_NOT);
        }
        ucUserAccountService.action(accountActionReq, loginUser);
        return GenericDto.success(null);
    }

    /**
     * 充/提币历史分页
     */
    @PostMapping("/action/history")
    @ApiOperation(value = "充/提币历史分页", notes = "充/提币历史分页")
    public GenericDto<IPage<AccountActionResp>> actionHistory(@RequestBody AccountActionHistoryReq historyReq) {
        Page page = new Page();
        page.setCurrent(historyReq.getCurrent());
        page.setSize(historyReq.getSize());
        return GenericDto.success(ucUserAccountService.actionHistory(page, historyReq));
    }
    /**
     * 创建地址
     */
    @PostMapping("/create/address")
    @ApiOperation(value = "创建地址", notes = "创建地址")
    public GenericDto<Object> createAddress(@RequestBody UcUserAddressReq addressReq, HttpServletRequest request) {
        long currentTime = System.currentTimeMillis();
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUserAddress build = UcUserAddress.builder()
                .build();
        BeanUtil.copyProperties(addressReq, build);
        build.setUserId(loginUser.getUserId());
        build.setCreateTime(currentTime);
        build.setUpdateTime(currentTime);
        ucUserAddressService.save(build);
        return GenericDto.success(null);
    }
}
