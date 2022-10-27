package com.seeds.account.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.account.dto.AddressCollectHisDto;
import com.seeds.account.dto.AddressCollectOrderHisDto;
import com.seeds.account.model.AddressCollectHis;
import com.seeds.account.model.AddressCollectOrderHis;
import com.seeds.common.dto.PagedDto;
import com.seeds.common.enums.Chain;

import java.util.List;

public interface IAddressCollectHisService {

    /**
     * 插入历史
     *
     * @param addressCollectHis
     */
    void createHistory(AddressCollectHis addressCollectHis);

    /**
     * 获取历史
     *
     * @param chain
     * @param startTime
     * @param endTime
     * @param fromAddress
     * @param toAddress
     * @param page
     * @param size
     * @return
     */
    IPage<AddressCollectHisDto> getHistory(Chain chain, long startTime, long endTime, String fromAddress, String toAddress, int page, int size);


    /**
     * 插入历史
     *
     * @param addressCollectOrderHis
     */
    void createOrderHistory(AddressCollectOrderHis addressCollectOrderHis);

    /**
     * @param chain
     * @param startTime
     * @param endTime
     * @param type
     * @param address
     * @param currency
     * @param page
     * @param size
     * @return
     */
    IPage<AddressCollectOrderHisDto> getOrderHistory(Chain chain, long startTime, long endTime, int type, String address, String currency, int page, int size);

    AddressCollectOrderHisDto getAddressCollectOrderById(long orderId);

    List<AddressCollectHisDto> getAddressCollectByOrderId(long orderId);
}
