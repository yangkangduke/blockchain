package com.seeds.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "划转并归集requestDto")
public class MgtGasFeeAndCollectOrderRequestDto {

    @NotNull
    @ApiModelProperty("1 eth; 3 tron")
    Integer chain;

    @NotEmpty
    List<MgtGasFeeAndCollectOrderDetail> list = Lists.newLinkedList();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "用户勾选的list")
    public static class MgtGasFeeAndCollectOrderDetail {
        @NotBlank
        @ApiModelProperty("用户地址")
        String address;

        @NotBlank
        @ApiModelProperty("链上余额")
        String chainBalance;

        @NotBlank
        @ApiModelProperty("链上USDT余额")
        String usdtBalance;
    }
}
