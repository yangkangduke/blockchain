package com.seeds.uc.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.response.UcUserAccountAmountResp;
import com.seeds.uc.service.IUcUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户账户表 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-07-09
 */
@RestController
@RequestMapping("/internal-account")
@Api(tags = "账户")
public class InterUserAccountController {

    @Autowired
    private IUcUserAccountService ucUserAccountService;

    @GetMapping("/amount-info")
    @ApiOperation(value = "账户金额详情", notes = "账户金额详情")
    @Inner
    public GenericDto<List<UcUserAccountAmountResp>> amountInfo(@RequestParam Long userId) {
        return GenericDto.success(ucUserAccountService.amountInfo(userId));
    }
}
