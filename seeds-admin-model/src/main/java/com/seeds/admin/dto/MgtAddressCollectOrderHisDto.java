package com.seeds.admin.dto;

import com.seeds.account.enums.FundCollectOrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel()
public class MgtAddressCollectOrderHisDto {
    @ApiModelProperty(value = "id")
    long id;
    @ApiModelProperty(value = "创建时间")
    long createTime;
    @ApiModelProperty(value = "更新时间")
    long updateTime;
    @ApiModelProperty(value = "币种时间")
    String currency;
    @ApiModelProperty(value = "地址")
    String address;
    @ApiModelProperty(value = "金额")
    String amount;
    @ApiModelProperty(value = "手续费")
    String feeAmount;
    /**
     * @see FundCollectOrderStatus
     */
    @ApiModelProperty(value = "1 已完成  3 进行中")
    int status;
}
