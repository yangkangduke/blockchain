package com.seeds.account.dto;

import lombok.Builder;
import lombok.Data;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;

import java.util.List;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/10
 */
@Data
@Builder
public class ChainAnchorPriceView {
    private Uint timeElapsed;
    private Uint rawUniswapPriceMantissa;
    private Uint conversionFactor;
    private Uint anchorPrice;

    public static ChainAnchorPriceView createInstance(List<Type> params) {
        return ChainAnchorPriceView.builder()
                .timeElapsed((Uint) params.get(0))
                .rawUniswapPriceMantissa((Uint) params.get(1))
                .conversionFactor((Uint) params.get(2))
                .anchorPrice((Uint) params.get(3))
                .build();
    }
}
