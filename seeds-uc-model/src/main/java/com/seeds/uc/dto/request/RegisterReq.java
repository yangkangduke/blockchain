package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seeds.uc.enums.ClientAppEnum;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/25
 */
@Data
public class RegisterReq {
    private ClientAppEnum appCode;
    private Long countryId;
    @JsonProperty("country_code")
    private String countryCode;
    private String nationality;
    private String password;
    private String email;
    private String phone;
    @JsonProperty("auth_code")
    private String authCode;
}
