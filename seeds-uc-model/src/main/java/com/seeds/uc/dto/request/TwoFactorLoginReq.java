package com.seeds.uc.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TwoFactorLoginReq {
    @NotBlank
    private String token;
    @NotBlank
    private String authCode;
}
