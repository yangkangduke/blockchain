package com.seeds.admin.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.MgtApproveRejectDto;
import com.seeds.admin.dto.MgtDepositWithdrawDto;
import com.seeds.common.dto.GenericDto;

public interface ISysWithdrawDepositService {


    Page<MgtDepositWithdrawDto> listPendingWithdraw(String currency, Long userId, Integer page, Integer size);

    GenericDto<Boolean> approvePendingWithdraw(MgtApproveRejectDto dto);

    GenericDto<Boolean> rejectPendingWithdraw(MgtApproveRejectDto dto);

    Page<MgtDepositWithdrawDto> listWithdrawReviewHis(String currency, Long userId, Integer status, Integer page, Integer size);

    Page<MgtDepositWithdrawDto> listPendingDeposit(String currency, Long userId, Integer page, Integer size);

    GenericDto<Boolean> approvePendingDeposit(MgtApproveRejectDto dto);

    GenericDto<Boolean> rejectPendingDeposit(MgtApproveRejectDto dto);

    Page<MgtDepositWithdrawDto> listDepositReviewHis(String currency, Long userId, Integer status, Integer page, Integer size);
}
