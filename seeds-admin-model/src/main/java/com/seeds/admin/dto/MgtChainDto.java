package com.seeds.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MgtChainDto {

    @ApiModelProperty(value = "1 eth， 3 tron")
    @NotNull
    private Integer chain;
}
