package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.enums.DepositStatus;
import com.seeds.admin.dto.MgtDepositWithdrawDto;
import com.seeds.admin.service.MgtWithdrawDepositService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@RestController
@RequestMapping("/mgt/cash")
@Api(tags = "cash")
public class MgtCashController {


    @Autowired
    private MgtWithdrawDepositService mgtWithdrawDepositService;



    @GetMapping("/pending-withdraw/list")
    @ApiOperation("获取提币申请")
    // todo
//    @MgtAuthority(path = "/funds/withdraw-deposit/withdraw/reviews/pending/")
    public GenericDto<IPage<MgtDepositWithdrawDto>> listPendingWithdraw(@RequestParam(value = "currency", required = false) String currency,
                                                                              @RequestParam(value = "userId", required = false) Long userId,
                                                                              @RequestParam("current") Integer current,
                                                                              @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listPendingWithdraw(currency, userId, current, size));

    }
}
