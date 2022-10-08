package com.seeds.account.dto;

import lombok.Builder;
import lombok.Data;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint8;

import java.util.List;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/5/14
 */

@Data
@Builder
public class ChainKTokenConfigDto {
    // kToken 地址
    private Address kToken;
    // underlying 地址
    private Address underlying;
    // underlying 的decimals
    private Uint baseUnit;
    // 价格源的 decimals
    private Uint priceUnit;
    // 价格源
    private Uint8 priceSource;

    public static ChainKTokenConfigDto createInstance(List<Type> params) {
        return ChainKTokenConfigDto.builder()
                .kToken((Address) params.get(0))
                .underlying((Address) params.get(1))
                .baseUnit((Uint) params.get(2))
                .priceUnit((Uint) params.get(3))
                .priceSource((Uint8) params.get(4))
                .build();
    }
}
