package com.seeds.account.dto;

import lombok.Builder;
import lombok.Data;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;

import java.util.List;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/10
 */
@Data
@Builder
public class ChainOracleHelperPriceDto {
    private Uint timestamp;
    private Utf8String symbol;
    private Uint price;

    public static ChainOracleHelperPriceDto createInstance(List<Type> params) {
        return ChainOracleHelperPriceDto.builder()
                .timestamp((Uint) params.get(0))
                .symbol((Utf8String) params.get(1))
                .price((Uint) params.get(2)).build();
    }
}
