package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.MgtApproveRejectDto;
import com.seeds.admin.dto.MgtDepositWithdrawDto;
import com.seeds.admin.service.ISysWithdrawDepositService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/cash")
@Api(tags = "cash")
public class SysCashController {


    @Autowired
    private ISysWithdrawDepositService mgtWithdrawDepositService;



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

    @PostMapping("/pending-withdraw/approve")
    @ApiOperation("批准提币")
//    @MgtAuthority(path = "/funds/withdraw-deposit/withdraw/reviews/pending/:approve")
    public GenericDto<Boolean> approveWithdraw(@RequestBody @Valid MgtApproveRejectDto dto) {
        return mgtWithdrawDepositService.approvePendingWithdraw(dto);
    }

    @PostMapping("/pending-withdraw/reject")
    @ApiOperation("拒绝提币")
//    @MgtAuthority(path = "/funds/withdraw-deposit/withdraw/reviews/pending/:reject")
    public GenericDto<Boolean> rejectWithdraw(@RequestBody @Valid MgtApproveRejectDto dto) {
        return mgtWithdrawDepositService.rejectPendingWithdraw(dto);
    }

    @GetMapping("/withdraw-history/list")
    @ApiOperation("获取提币审批历史")
//    @MgtAuthority(path = "/funds/withdraw-deposit/withdraw/reviews/history/")
    public GenericDto<Page<MgtDepositWithdrawDto>> listWithdrawHis(@RequestParam(value = "currency", required = false) String currency,
                                                                               @RequestParam(value = "usdId", required = false) Long userId,
                                                                               @RequestParam(value = "status", defaultValue = "0") Integer status,
                                                                               @RequestParam("current") Integer current,
                                                                               @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listWithdrawReviewHis(currency, userId, status, current, size));
    }

    @GetMapping("/pending-deposit/list")
    @ApiOperation("获取充币申请")
//    @MgtAuthority(path = "/funds/withdraw-deposit/deposit/reviews/pending/")
    public GenericDto<Page<MgtDepositWithdrawDto>> listPendingDeposit(@RequestParam(value = "currency", required = false) String currency,
                                                                                  @RequestParam(value = "userId", required = false) Long userId,
                                                                                  @RequestParam("current") Integer current,
                                                                                  @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listPendingDeposit(currency, userId, current, size));
    }

    @PostMapping("/pending-deposit/approve")
    @ApiOperation("批准充币")
//    @MgtAuthority(path = "/funds/withdraw-deposit/deposit/reviews/pending/:approve")
    public GenericDto<Boolean> approveDeposit(@RequestBody @Valid MgtApproveRejectDto dto) {
        dto.setComment("");
        return mgtWithdrawDepositService.approvePendingDeposit(dto);
    }

    @PostMapping("/pending-deposit/reject")
    @ApiOperation("拒绝充币")
//    @MgtAuthority(path = "/funds/withdraw-deposit/deposit/reviews/pending/:reject")
    public GenericDto<Boolean> rejectDeposit(@RequestBody @Valid MgtApproveRejectDto dto) {
        return mgtWithdrawDepositService.rejectPendingDeposit(dto);
    }

    @GetMapping("/deposit-history/list")
    @ApiOperation("获取充币审批历史")
//    @MgtAuthority(path = "/funds/withdraw-deposit/deposit/reviews/history/")
    public GenericDto<Page<MgtDepositWithdrawDto>> listDepositHis(@RequestParam(value = "currency", required = false) String currency,
                                                                              @RequestParam(value = "userId", required = false) Long userId,
                                                                              @RequestParam(value = "status", required = false) Integer status,
                                                                              @RequestParam("current") Integer current,
                                                                              @RequestParam("size") Integer size) {
        return GenericDto.success(mgtWithdrawDepositService.listDepositReviewHis(currency, userId, status, current, size));
    }
}
