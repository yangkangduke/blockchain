package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.AddressCollectHisDto;
import com.seeds.account.dto.AddressCollectOrderHisDto;
import com.seeds.account.mapper.AddressCollectHisMapper;
import com.seeds.account.mapper.AddressCollectOrderHisMapper;
import com.seeds.account.model.AddressCollectHis;
import com.seeds.account.model.AddressCollectOrderHis;
import com.seeds.account.service.IAddressCollectHisService;
import com.seeds.account.util.ObjectUtils;
import com.seeds.account.util.Utils;
import com.seeds.common.enums.Chain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author milo
 */
@Slf4j
@Service
public class AddressCollectHisServiceImpl implements IAddressCollectHisService {

    @Autowired
    AddressCollectHisMapper addressCollectHisMapper;

    @Autowired
    AddressCollectOrderHisMapper addressCollectOrderHisMapper;

    @Override
    public void createHistory(AddressCollectHis addressCollectHis) {
        log.info("createHistory addressCollectHis={}", addressCollectHis);
        addressCollectHisMapper.insert(addressCollectHis);
    }

    @Override
    public IPage<AddressCollectHisDto> getHistory(Chain chain, long startTime, long endTime, String fromAddress, String toAddress, int page, int size) {
        Utils.checkPage(page, size);
        Page queryPage = new Page();
        queryPage.setCurrent(page);
        queryPage.setSize(size);
        return addressCollectHisMapper.getByAddress(queryPage, chain.getCode(), startTime, endTime, fromAddress, toAddress);
    }

    @Override
    public void createOrderHistory(AddressCollectOrderHis addressCollectOrderHis) {
        log.info("createOrderHistory addressCollectOrderHis={}", addressCollectOrderHis);
        addressCollectOrderHisMapper.insert(addressCollectOrderHis);
    }

    @Override
    public IPage<AddressCollectOrderHisDto> getOrderHistory(Chain chain, long startTime, long endTime, int type, String address, String currency, int page, int size) {
        Utils.checkPage(page, size);

        Page queryPage = new Page();
        queryPage.setCurrent(page);
        queryPage.setSize(size);
        IPage<AddressCollectOrderHisDto> list = addressCollectOrderHisMapper.getList(queryPage, chain.getCode(), startTime, endTime, type, address, currency);
        return list;
    }

    @Override
    public AddressCollectOrderHisDto getAddressCollectOrderById(long orderId) {
        AddressCollectOrderHis orderHis = addressCollectOrderHisMapper.selectByPrimaryKey(orderId);
        return orderHis != null ? ObjectUtils.copy(orderHis, new AddressCollectOrderHisDto()) : null;
    }

    @Override
    public List<AddressCollectHisDto> getAddressCollectByOrderId(long orderId) {
        List<AddressCollectHis> list = addressCollectHisMapper.getByOrderId(orderId);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    private AddressCollectHisDto toDto(AddressCollectHis addressCollectHis) {
        return ObjectUtils.copy(addressCollectHis, new AddressCollectHisDto());
    }

    private AddressCollectOrderHisDto toOrderDto(AddressCollectOrderHis addressCollectOrderHis) {
        return ObjectUtils.copy(addressCollectOrderHis, new AddressCollectOrderHisDto());
    }
}
