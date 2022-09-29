package com.seeds.account.chain.service;

import com.seeds.account.dto.AddressBalanceDto;
import org.web3j.protocol.core.Response;

import java.util.Map;

@FunctionalInterface
public interface BatchBalanceResponseProcessor {
    void process(Map<String, AddressBalanceDto> balanceMap, Response response);
}