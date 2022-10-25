package com.seeds.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.seeds.account.dto.ChainDepositWithdrawHisDto;
import com.seeds.account.dto.req.AccountPendingTransactionsReq;
import com.seeds.account.enums.ChainAction;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.dto.MgtDepositWithdrawDto;
import com.seeds.admin.mapstruct.DepositWithdrawDtoMapper;
import com.seeds.admin.service.MgtWithdrawDepositService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.dto.PagedDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Slf4j
public class MgtWithdrawDepositServiceImpl implements MgtWithdrawDepositService {

    @Autowired
    private AccountFeignClient accountFeignClient;
    @Autowired
    private DepositWithdrawDtoMapper depositWithdrawDtoMapper;

    @Override
    public IPage<MgtDepositWithdrawDto> listPendingWithdraw(String currency, Long userId, Integer page, Integer size) {
        AccountPendingTransactionsReq transactionsReq = new AccountPendingTransactionsReq();
        transactionsReq.setAction(ChainAction.WITHDRAW.getCode());
        transactionsReq.setCurrency(currency);
        transactionsReq.setUserId(userId);
        transactionsReq.setCurrent(page);
        transactionsReq.setSize(size);
        GenericDto<Page<ChainDepositWithdrawHisDto>> dto = accountFeignClient.getPendingTransactions(transactionsReq);
        if (!dto.isSuccess()) {
            return new Page<>();
        }
        IPage<MgtDepositWithdrawDto> pageList = new Page<>();
        List<MgtDepositWithdrawDto> mgtDepositWithdrawDtos = depositWithdrawDtoMapper.convert2MgtDepositWithdrawDtos(dto.getData().getRecords());
        pageList.setRecords(mgtDepositWithdrawDtos);
        pageList.setCurrent(dto.getData().getCurrent());
        pageList.setSize(dto.getData().getSize());
        pageList.setPages(dto.getData().getPages());
        pageList.setTotal(dto.getData().getTotal());
        return pageList;
    }
}
