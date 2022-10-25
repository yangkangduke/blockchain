package com.seeds.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.MgtDepositWithdrawDto;

import java.util.List;

public interface MgtWithdrawDepositService {


    IPage<MgtDepositWithdrawDto> listPendingWithdraw(String currency, Long userId, Integer page, Integer size);
}
