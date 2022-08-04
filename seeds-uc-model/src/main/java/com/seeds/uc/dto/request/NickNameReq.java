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
public class NickNameReq {
    @ApiModelProperty(value = "昵称")
    @NotBlank
    private String nickname;
}
