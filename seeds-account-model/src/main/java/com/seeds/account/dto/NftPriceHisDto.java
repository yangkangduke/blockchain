package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author yk
 *
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class NftPriceHisDto implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "平均价格")
    private BigDecimal ovgAmount;

    @ApiModelProperty(value = "数据")
    private List<PriceHis> list;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceHis implements Serializable {

        @ApiModelProperty(value = "日期")
        private String date;

        @ApiModelProperty(value = "数量")
        private Integer number;

        @ApiModelProperty(value = "平均价格")
        private BigDecimal ovgPrice;

        @ApiModelProperty(value = "总价格")
        private BigDecimal totalPrice;

    }

}
