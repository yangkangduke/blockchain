package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TwoFactorLoginReq {
    private String token;
    @JsonProperty("auth_code")
    private String authCode;
}
