package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: seeds-java
 * @description: GA
 * @author: yk
 * @create: 2022-08-04 13:35
 **/
@Data
public class FacebookReq {
    @ApiModelProperty(value = "facebook地址")
    @NotBlank
    private String facebook;
}
