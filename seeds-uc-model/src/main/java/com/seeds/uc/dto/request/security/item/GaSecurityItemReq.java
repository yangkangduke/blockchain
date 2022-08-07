package com.seeds.uc.dto.request.security.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
@Data
public class GaSecurityItemReq {
    @JsonProperty("ga_code")
    private String gaCode;
    @JsonProperty("ga_token")
    private String gaToken;
//    @JsonProperty("auth_token")
//    private String authToken;
}
