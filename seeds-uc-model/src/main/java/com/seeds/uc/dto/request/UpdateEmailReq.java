package com.seeds.uc.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: seeds-java
 * @description: 修改邮箱
 * @author: yk
 * @create: 2022-08-04 13:35
 **/
@Data
@Builder
public class UpdateEmailReq {
    @NotBlank
    private String authToken;
    @NotBlank
    private String gaCode;
}
